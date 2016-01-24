package com.marssoft.testapp.pojo;

import java.util.Date;

/**
 * Created by alexey on 24-Jan-16.
 */
public class NetworkResponse {
    String message;
    String leadId;
    Date created;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getLeadId() {
        return leadId;
    }

    public void setLeadId(String leadId) {
        this.leadId = leadId;
    }

    public Date getCreated() {
        return created;
    }

    public void setCreated(Date created) {
        this.created = created;
    }
}
