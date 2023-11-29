package com.jackmu.scemail.model;

public class EntryEmailDTOImpl implements EntryEmailDTO{
    private String entryText;
    private String subscriberEmail;
    private String seriesTitle;
    private String entryTitle;

    @Override
    public String getEntryText() {
        return entryText;
    }
    @Override
    public String getSubscriberEmail() {
        return subscriberEmail;
    }
    @Override
    public String getEntryTitle() {
        return entryTitle;
    }

    @Override
    public String getSeriesTitle(){ return seriesTitle; }
}
