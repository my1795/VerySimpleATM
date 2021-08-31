package com.neueda.atm.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.util.Objects;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "account")
public class AccountEntity extends BaseEntity{
    @Column(unique=true)
    protected long accountNumber;

    @Column
    protected int pin;

    @Column
    protected double balance;

    @Column
    protected double overdraft;

    public long getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(long accountNumber) {
        this.accountNumber = accountNumber;
    }

    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountEntity)) return false;
        AccountEntity that = (AccountEntity) o;
        return getAccountNumber() == that.getAccountNumber() && getPin() == that.getPin() && Double.compare(that.getBalance(), getBalance()) == 0 && Double.compare(that.getOverdraft(), getOverdraft()) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAccountNumber(), getPin(), getBalance(), getOverdraft());
    }

    @Override
    public String toString() {
        return "AccountEntity{" +
                "accountNumber=" + accountNumber +
                ", pin=" + pin +
                ", balance=" + balance +
                ", overdraft=" + overdraft +
                ", id=" + id +
                '}';
    }
}
