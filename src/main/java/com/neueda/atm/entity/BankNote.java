package com.neueda.atm.entity;

import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Entity;
import javax.persistence.EntityListeners;
import javax.persistence.Table;
import java.util.Objects;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof BankNote)) return false;
        BankNote bankNote = (BankNote) o;
        return getAmount() == bankNote.getAmount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getAmount());
    }

    @Override
    public String toString() {
        return "BankNote{" +
                "amount=" + amount +
                ", id=" + id +
                '}';
    }
}
