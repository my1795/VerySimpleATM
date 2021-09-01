package com.neueda.atm.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.neueda.atm.common.constant.RequestType;

import java.io.Serializable;
import java.util.List;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountResponse  implements Serializable {
    private RequestType requestType;
    private long accountNumber;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    private double remainingBalance;

    private double balance;

    private double overdraft;
    @JsonInclude(JsonInclude.Include.NON_DEFAULT)
    List<BankNoteResource> withdrawalBankNotes;


    public RequestType getRequestType() {
        return requestType;
    }

    public void setRequestType(RequestType requestType) {
        this.requestType = requestType;
    }

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public double getRemainingBalance() {
        return remainingBalance;
    }

    public void setRemainingBalance(double remainingBalance) {
        this.remainingBalance = remainingBalance;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public double getOverdraft() {
        return overdraft;
    }

    public void setOverdraft(double overdraft) {
        this.overdraft = overdraft;
    }

    public List<BankNoteResource> getWithdrawalBankNotes() {
        return withdrawalBankNotes;
    }

    public void setWithdrawalBankNotes(List<BankNoteResource> withdrawalBankNotes) {
        this.withdrawalBankNotes = withdrawalBankNotes;
    }
}
