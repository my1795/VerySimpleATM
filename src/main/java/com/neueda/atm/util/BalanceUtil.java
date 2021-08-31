package com.neueda.atm.util;

import com.neueda.atm.entity.BankNote;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class BalanceUtil {
    public static Map<BankNote, Integer> getMinBankNotesByWithdrawAmount(Map<BankNote, Integer> atmBalance,  int withdrawAmount) {
        Map<BankNote, Integer> withdraw = new HashMap<>();
        AtomicInteger atomicWithdraw = new AtomicInteger();
        atomicWithdraw.set(withdrawAmount);
        atmBalance.keySet().stream().sorted().forEach(bankNote -> {
            if(atomicWithdraw.get() < (bankNote).getAmount() * atmBalance.get(bankNote) && atomicWithdraw.get() > 0){
                withdraw.put(bankNote, atmBalance.get(bankNote));
                atomicWithdraw.getAndSet(atomicWithdraw.get()-(bankNote).getAmount() * atmBalance.get(bankNote) );

            }
        });
        return withdraw;
    }
}
