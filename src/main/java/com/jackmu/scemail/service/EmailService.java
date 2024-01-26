package com.jackmu.scemail.service;

import com.jackmu.scemail.model.EntryEmailDTO;

import java.util.List;

public interface EmailService {
    public void sendEmails(List<EntryEmailDTO> entryEmailDTOList);
    public void scheduleSendEmails();
    public void deleteFinishedSeries();
}
