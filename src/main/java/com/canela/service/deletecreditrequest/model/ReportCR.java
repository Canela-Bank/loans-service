package com.canela.service.deletecreditrequest.model;

import javax.persistence.Entity;

public class ReportCR {
    private String id;
    private String type;
    private String bank;
    private String status;
    private String typeDocument;
    private String clientDocument;

    public ReportCR() {
    }

    public ReportCR(String id, String type, String bank, String status, String typeDocument, String clientDocument) {
        this.id = id;
        this.type = type;
        this.bank = bank;
        this.status = status;
        this.typeDocument = typeDocument;
        this.clientDocument = clientDocument;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBank() {
        return bank;
    }

    public void setBank(String bank) {
        this.bank = bank;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTypeDocument() {
        return typeDocument;
    }

    public void setTypeDocument(String typeDocument) {
        this.typeDocument = typeDocument;
    }

    public String getClientDocument() {
        return clientDocument;
    }

    public void setClientDocument(String clientDocument) {
        this.clientDocument = clientDocument;
    }
}
