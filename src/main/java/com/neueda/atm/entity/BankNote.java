package com.neueda.atm.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "banknote")
public class BankNote extends BaseEntity  implements Comparable<BankNote>{
    protected int amount;

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    @Override
    public int compareTo(BankNote o) {
        return Integer.compare(o.getAmount(), amount);
    }
}
