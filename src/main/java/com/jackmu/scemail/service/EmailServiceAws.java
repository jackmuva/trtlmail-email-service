package com.jackmu.scemail.service;

import com.jackmu.scemail.model.Subscription;
import com.jackmu.scemail.repository.SubscriptionRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Profile;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

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
        for (EntryEmailDTO entryEmail : entryEmailDTOList) {
            try {
                MimeMessage message = mailSender.createMimeMessage();
                MimeMessageHelper helper = new MimeMessageHelper(message);
                helper.setBcc(getEmails(entryEmail));
                helper.setFrom("jackmu@umich.edu", "My email address");
                helper.setSubject(entryEmail.getSeriesTitle() + " : " + entryEmail.getEntryTitle());
                helper.setText(parseEmails(entryEmail.getEntryText()), true);

                mailSender.send(message);
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public String[] getEmails(EntryEmailDTO entryEmail){
        List<String> emails = new ArrayList<>();
        List<Subscription> subscriptions = subscriptionRepository.findAllByArticleNumAndSeriesId(entryEmail.getArticleNum(), entryEmail.getSeriesId());
        for(Subscription subscription : subscriptions){
            emails.add(subscription.getSubscriberEmail());
        }
        return emails.toArray(new String[0]);
    }

    public String parseEmails(String html){
        String resHtml = html.substring(2, html.length() - 2);

        resHtml = resHtml.replaceAll("\",\"(?=[^>]*>)", "");
        resHtml = resHtml.replace("\\\"", "\"");

        return resHtml;
    }

    @Override
    @Scheduled(cron = "0 0 6 * * *")
    public void scheduleSendEmails(){
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
    public void incrementArticleNum(){
        subscriptionRepository.incrementArticleNum();
    }

    @Override
    @Scheduled(cron = "0 0 12 * * *")
    public void deleteFinishedSeries(){
        subscriptionRepository.deleteFinishedSubscriptions();
    }

}
