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
                helper.setFrom("trtlpost@trtlpost.com", "Trtlpost");
                helper.setSubject(entryEmail.getSeriesTitle() + " : " + entryEmail.getEntryTitle());
                helper.setText(appendUnsubscribeHtml(parseEmails(entryEmail.getEntryText()), entryEmail.getSeriesId()), true);

                mailSender.send(message);

                subscriptionRepository.updateSendDate(entryEmail.getArticleNum(), entryEmail.getSeriesId());
                subscriptionRepository.incrementArticleNum(entryEmail.getArticleNum(), entryEmail.getSeriesId());
            } catch(Exception e){
                e.printStackTrace();
            }
        }
    }

    public String appendUnsubscribeHtml(String emailHtml, Long seriesId){
        String unsubMessage = "<br/><br/><p>If you'd like to unsubscribe from this series, please click on the link " +
                "below and input the <strong>Series Id: " + seriesId + "<strong/></p>" + "<p><a href = \"https://trtlpost.com/unsubscribe\"> " +
                "https://trtlpost.com/unsubscribe</a></p>";
        return emailHtml + unsubMessage;
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
    @Scheduled(cron = "0 0 8 * * *")
    public void deleteFinishedSeries(){
        subscriptionRepository.deleteFinishedSubscriptions();
    }

}
