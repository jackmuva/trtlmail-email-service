package com.jackmu.scemail.service;

import com.jackmu.scemail.model.EntryEmailDTO;

import java.util.List;

public interface EmailService {
    void sendEmails(List<EntryEmailDTO> entryEmailDTOList);
    void scheduleSendEmails();
    void decrementReaderCount();
    void deleteFinishedSeries();
}
