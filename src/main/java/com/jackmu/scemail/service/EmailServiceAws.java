package com.jackmu.scemail.service;

import com.jackmu.scemail.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.jackmu.scemail.model.EntryEmailDTO;

import javax.mail.internet.MimeMessage;

@Service
@Profile("!local")
public class EmailServiceAws implements EmailService{
    @Autowired
    private SubscriptionRepository subscriptionRepository;
    @Autowired
    private JavaMailSender mailSender;
    private static final Logger LOGGER = Logger.getLogger(EmailServiceAws.class.getName());


    public void sendEmails(List<EntryEmailDTO> entryEmailDTOList) {
        LOGGER.info("sending email");
        for (EntryEmailDTO entryEmail : entryEmailDTOList) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);
                helper.setFrom("jackmu@umich.edu", "My email address");
                helper.setTo(entryEmail.getSubscriberEmail());
                helper.setSubject(entryEmail.getSeriesTitle() + " : " + entryEmail.getEntryTitle());
                helper.setText(parseEmails(entryEmail.getEntryText()), true);

                mailSender.send(message);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public String parseEmails(String html){
        String resHtml = html.substring(2, html.length() - 2);

        resHtml = resHtml.replaceAll("\",\"(?=[^>]*>)", "");

        return resHtml;
    }

    @Override
    @Scheduled(cron = "0 * * * * MON-FRI")
    public void scheduleSendEmails(){
        LOGGER.info("schedule sent");
        List<EntryEmailDTO> readyEmails = subscriptionRepository.findEmailsBySendDate();
        sendEmails(readyEmails);
    }

    @Override
    @Scheduled(cron = "0 0 10 * * *")
    public void updateSendDate(){
        subscriptionRepository.updateSendDate();
    }

    @Override
    @Scheduled(cron = "0 0 11 * * *")
    public void deleteFinishedSeries(){
        subscriptionRepository.deleteFinishedSubscriptions();
    }

    @Override
    @Scheduled(cron = "0 0 13 * * *")
    public void incrementArticleNum(){
        subscriptionRepository.incrementArticleNum();
    }
}
