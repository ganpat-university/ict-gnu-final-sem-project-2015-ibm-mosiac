package com.example.vaibh.mosaic;


import java.io.Serializable;

public class ViewMt implements Serializable {

    private int meetingEmpId;
    private String contactedPerson;
    private String meetingDate;
    private String meetingDescription;



    public int getMeetingEmpId() {
        return meetingEmpId;
    }

    public void setMeetingEmpId(int meetingEmpId) {
        this.meetingEmpId = meetingEmpId;
    }

    public String getContactedPerson() {
        return contactedPerson;
    }

    public void setContactedPerson(String contactedPerson) {
        this.contactedPerson = contactedPerson;
    }

    public String getMeetingDate() {
        return meetingDate;
    }

    public void setMeetingDate(String meetingDate) {
        this.meetingDate = meetingDate;
    }

    public String getMeetingDescription() {
        return meetingDescription;
    }

    public void setMeetingDescription(String meetingDescription) {
        this.meetingDescription = meetingDescription;
    }
}
