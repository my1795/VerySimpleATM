package com.neueda.atm.service;

import com.neueda.atm.entity.Bank;
import com.neueda.atm.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.concurrent.atomic.AtomicInteger;

@Service
public class ATMServiceImpl implements ATMService{



    BankRepository bankRepository;

    @Autowired
    public ATMServiceImpl(BankRepository bankRepository) {
        this.bankRepository = bankRepository;
    }

    @Override
    public int getATMBalance() {
        Bank bank = bankRepository.findBankByNameEquals("atm1");
        AtomicInteger totalBalance = new AtomicInteger();
        bank.getBalanceMap().forEach((bankNote, count) -> {
            totalBalance.getAndAdd(bankNote.getAmount()*count);
        });
        return totalBalance.get();
    }
}
