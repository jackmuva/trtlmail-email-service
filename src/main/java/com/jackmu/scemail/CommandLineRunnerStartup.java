package com.jackmu.scemail;


import com.jackmu.scemail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class CommandLineRunnerStartup implements CommandLineRunner {
    @Autowired
    private EmailService emailService;

    @Override
    public void run(String...args) throws Exception {
        emailService.scheduleSendEmails();
        emailService.decrementReaderCount();
        emailService.deleteFinishedSeries();
    }
}

