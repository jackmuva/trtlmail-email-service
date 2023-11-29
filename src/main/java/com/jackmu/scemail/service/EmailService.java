package com.jackmu.scemail.service;

import com.jackmu.scemail.model.EntryEmailDTO;

import java.util.List;

public interface EmailService {
    public void setLocalMode(Boolean localMode);
    public void sendEmails(List<EntryEmailDTO> entryEmailDTOList);
    public void sendLogEmails(List<EntryEmailDTO> entryEmailDTOList);
    public void sendRealEmail(List<EntryEmailDTO> entryEmailDTOList);
    public void scheduleSendEmails();
    public void updateSendDate();
    public void deleteFinishedSeries();
    public void incrementArticleNum();
}
