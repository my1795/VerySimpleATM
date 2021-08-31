package com.neueda.atm.config;

import com.neueda.atm.entity.AccountEntity;
import com.neueda.atm.entity.Bank;
import com.neueda.atm.entity.BankNote;
import com.neueda.atm.repository.AccountRepository;
import com.neueda.atm.repository.BankNoteRepository;
import com.neueda.atm.repository.BankRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.*;

@Configuration
public class InitDatabase {
    @Autowired
    AccountRepository accountRepository;
    @Autowired
    BankNoteRepository bankNoteRepository;
    @Autowired
    BankRepository bankRepository;
    public InitDatabase() {}

    @PostConstruct
    public void init(){
        AccountEntity accountEntity = new AccountEntity();
        accountEntity.setAccountNumber(123456789);
        accountEntity.setBalance(800);
        accountEntity.setOverdraft(200);
        accountEntity.setPin(1234);
        AccountEntity accountEntity2 = new AccountEntity();
        accountEntity2.setAccountNumber(987654321);
        accountEntity2.setBalance(1230);
        accountEntity2.setOverdraft(150);
        accountEntity2.setPin(4321);
        List<AccountEntity> accountEntities = new ArrayList<>();
        accountEntities.add(accountEntity);
        accountEntities.add(accountEntity2);
        accountRepository.saveAll(accountEntities);

        BankNote bankNote5 = new BankNote();
        bankNote5.setAmount(5);
        BankNote bankNote10 = new BankNote();
        bankNote10.setAmount(10);
        BankNote bankNote20 = new BankNote();
        bankNote20.setAmount(20);
        BankNote bankNote50 = new BankNote();
        bankNote50.setAmount(50);
        List<BankNote> bankNotes = new ArrayList<>();
        bankNotes.addAll(Arrays.asList(new BankNote[]{bankNote5, bankNote10, bankNote20, bankNote50}));
        bankNoteRepository.saveAll(bankNotes);

        Bank bank = new Bank();

        Map<BankNote,Integer> bankNoteIntegerMap = new HashMap<>();
        bankNoteIntegerMap.put(bankNote5,20);
        bankNoteIntegerMap.put(bankNote10, 30);
        bankNoteIntegerMap.put(bankNote20, 30);
        bankNoteIntegerMap.put(bankNote50, 10);

        bank.setBalanceMap(bankNoteIntegerMap);
        bank.setName("atm1");

        bankRepository.save(bank);


    }
}
