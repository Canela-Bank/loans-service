package com.canela.service.loanmgmt.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
public class Loan {
    @Id
    private String id;
    private Double interest_rate;
    private Double min_payment;
    private Double balance;
    private String payment_date;
    private Double debt;
    private String user_id;
    private Integer user_document_type;

    public Loan(@JsonProperty("id") String id,
                 @JsonProperty("interest_rate") Double interest_rate,
                 @JsonProperty("min_payment") Double min_payment,
                 @JsonProperty("balance") Double balance,
                 @JsonProperty("payment_date") String payment_date,
                 @JsonProperty("debt") Double debt,
                 @JsonProperty("user_id") String user_id,
                 @JsonProperty("user_document_type") Integer user_document_type) {
        this.id = id;
        this.interest_rate = interest_rate;
        this.min_payment = min_payment;
        this.balance = balance;
        this.payment_date = payment_date;
        this.debt = debt;
        this.user_id = user_id;
        this.user_document_type = user_document_type;
    }

    public Loan() {

    }
}