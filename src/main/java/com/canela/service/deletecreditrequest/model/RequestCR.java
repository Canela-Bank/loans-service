package com.canela.service.deletecreditrequest.model;

import javax.persistence.*;
import java.util.List;

public class RequestCR {

    private String document;
    private  String type;
    private String firstname;
    private String lastname;
    private String email;
    private String phone;
    private List<ReportCR> reports;

    public RequestCR() {
    }

    public RequestCR(String document, String type, String firstname, String lastname, String email, String phone, List<ReportCR> reports) {
        this.document = document;
        this.type = type;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phone = phone;
        this.reports = reports;
    }

    public String getDocument() {
        return document;
    }

    public void setDocument(String document) {
        this.document = document;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFirstname() {
        return firstname;
    }

    public void setFirstname(String firstname) {
        this.firstname = firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public void setLastname(String lastname) {
        this.lastname = lastname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public List<ReportCR> getReports() {
        return reports;
    }

    public void setReports(List<ReportCR> reports) {
        this.reports = reports;
    }
}
