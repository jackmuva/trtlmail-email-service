package com.jackmu.scemail.service;

import com.jackmu.scemail.repository.SubscriptionRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.*;
import javax.mail.*;
import javax.mail.internet.*;
import javax.mail.Session;
import javax.mail.Transport;

import java.util.List;
import java.util.logging.Logger;
import com.jackmu.scemail.model.EntryEmailDTO;

@Service
public class EmailServiceImpl implements EmailService{
    private Boolean localMode = false;
    private static final Logger LOGGER = Logger.getLogger(EmailServiceImpl.class.getName());
    private SubscriptionRepository subscriptionRepository;

    @Override
    public void setLocalMode(Boolean bool){
        localMode = bool;
    }

    @Override
    public void sendEmails(List<EntryEmailDTO> entryEmailDTOList){
        if(localMode){
            sendLogEmails(entryEmailDTOList);
        }
        else {
            sendRealEmail(entryEmailDTOList);
        }
    }

    @Override
    public void sendLogEmails(List<EntryEmailDTO> entryEmailDTOList){
        for(EntryEmailDTO entryEmail : entryEmailDTOList){
            String sender = "sender@gmail.com";
            String host = "127.0.0.1";
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);
            Session session = Session.getDefaultInstance(properties);
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(sender));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(entryEmail.getSubscriberEmail()));
                message.setSubject(entryEmail.getEntryTitle());
                message.setContent(entryEmail.getEntryText(),"text/html");

                LOGGER.info(message.getRecipients(Message.RecipientType.TO).toString());
                LOGGER.info(message.getContent().toString());
            }
            catch (Exception e) {
                LOGGER.warning("Failed to create message");
            }
        }
    }

    @Override
    public void sendRealEmail(List<EntryEmailDTO> entryEmailDTOList){
        for(EntryEmailDTO entryEmail : entryEmailDTOList){
            String sender = "sender@gmail.com";
            String host = "127.0.0.1";
            Properties properties = System.getProperties();
            properties.setProperty("mail.smtp.host", host);
            Session session = Session.getDefaultInstance(properties);
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress(sender));
                message.addRecipient(Message.RecipientType.TO, new InternetAddress(entryEmail.getSubscriberEmail()));
                message.setSubject(entryEmail.getEntryTitle());
                message.setContent(entryEmail.getEntryText(),"text/html");

                Transport.send(message);
            }
            catch (MessagingException mex) {
                mex.printStackTrace();
            }
        }
    }

    @Override
    @Scheduled(cron = "0 6 * * *")
    public void scheduleSendEmails(){
        List<EntryEmailDTO> readyEmails = subscriptionRepository.findEmailsBySendDate();
        setLocalMode(true);
        sendEmails(readyEmails);
    }

    @Override
    @Scheduled(cron = "0 10 * * *")
    public void updateSendDate(){
        subscriptionRepository.updateSendDate();
    }

    @Override
    @Scheduled(cron = "0 11 * * *")
    public void deleteFinishedSeries(){
        subscriptionRepository.deleteFinishedSubscriptions();
    }

    @Override
    @Scheduled(cron = "0 13 * * *")
    public void incrementArticleNum(){
        subscriptionRepository.incrementArticleNum();
    }
}
