package com.neueda.atm.util;

import com.neueda.atm.entity.BankNote;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

public class BalanceUtil {
    public static Map<BankNote, Integer> getMinBankNotesByWithdrawAmount(Map<BankNote, Integer> atmBalance,  int withdrawAmount) throws Exception {
       // Integer[] values = (Integer[] ) atmBalance.keySet().stream().map(bankNote -> bankNote.getAmount()).collect(Collectors.toList()).stream().sorted().toArray();
        int[] values = new int[atmBalance.keySet().size()];
        int[] amounts = new int[values.length];
        Map<BankNote,Integer> resultMap = new HashMap<>();
        AtomicInteger atomicInteger = new AtomicInteger(0);
        atmBalance.keySet().stream().forEach(bankNote -> {
            values[atomicInteger.get()] = bankNote.getAmount();
            atomicInteger.getAndIncrement();
        });



        int[] valDesc = Arrays.stream(values).boxed()
                .sorted(Collections.reverseOrder())
                .mapToInt(Integer::intValue)
                .toArray();

        for(int i=0; i< valDesc.length; i++){
            int finalI = i;
            atmBalance.forEach((bankNote, amount) -> {
                if(bankNote.getAmount() == valDesc[finalI]){
                    amounts[finalI] = amount;
                }
            });
        }


        List<Integer[]> combinations  = getCombinations(valDesc, amounts, new int[values.length], withdrawAmount, 0);
        if(combinations.isEmpty()){
            throw  new Exception("Cannot dispense with given amount");
        }
        Integer[] minimumBankNotes = combinations.get(0);

        for(int i=0; i< minimumBankNotes.length; i++){
            if(minimumBankNotes[i] != 0){
                BankNote bankNote = new BankNote();
                bankNote.setAmount(valDesc[i]);
                resultMap.put(bankNote, minimumBankNotes[i]);
            }
        }

        return resultMap;
    }



    public static List<Integer[]> getCombinations(int[] values, int[] ammounts, int[] variation, int price, int position){
        List<Integer[]> list = new ArrayList<>();
        int value = compute(values, variation);
        if (value < price){
            for (int i = position; i < values.length; i++) {
                if (ammounts[i] > variation[i]){
                    int[] newvariation = variation.clone();
                    newvariation[i]++;
                    List<Integer[]> newList = getCombinations(values, ammounts, newvariation, price, i);
                    if (newList != null){
                        list.addAll(newList);
                    }
                }
            }
        } else if (value == price) {
            list.add(myCopy(variation));
        }
        return list;
    }

    public static int compute(int[] values, int[] variation){
        int ret = 0;
        for (int i = 0; i < variation.length; i++) {
            ret += values[i] * variation[i];
        }
        return ret;
    }

    public static Integer[] myCopy(int[] ar){
        Integer[] ret = new Integer[ar.length];
        for (int i = 0; i < ar.length; i++) {
            ret[i] = ar[i];
        }
        return ret;
    }
}
