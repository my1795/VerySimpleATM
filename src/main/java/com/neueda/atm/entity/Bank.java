package com.neueda.atm.entity;

import org.hibernate.annotations.OptimisticLock;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.data.jpa.repository.Lock;

import javax.persistence.*;
import java.util.HashMap;
import java.util.Map;

@Entity
@EntityListeners(AuditingEntityListener.class)
@Table(name = "bank")
public class Bank extends BaseEntity {
    @Column
    private String name;
    @ElementCollection
    private Map<BankNote, Integer> balanceMap = new HashMap<BankNote, Integer>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Map<BankNote, Integer> getBalanceMap() {
        return balanceMap;
    }

    public void setBalanceMap(Map<BankNote, Integer> balanceMap) {
        this.balanceMap = balanceMap;
    }

}
