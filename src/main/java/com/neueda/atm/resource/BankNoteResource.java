package com.neueda.atm.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BankNoteResource implements Serializable {
    private double bankNoteAmount;
    private int bankNoteCount;

    public double getBankNoteAmount() {
        return bankNoteAmount;
    }

    public void setBankNoteAmount(double bankNoteAmount) {
        this.bankNoteAmount = bankNoteAmount;
    }

    public int getBankNoteCount() {
        return bankNoteCount;
    }

    public void setBankNoteCount(int bankNoteCount) {
        this.bankNoteCount = bankNoteCount;
    }
}
