package com.neueda.atm.service;

import com.neueda.atm.entity.Bank;
import com.neueda.atm.entity.BankNote;
import com.neueda.atm.repository.BankRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.lenient;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
public class ATMServiceImplUT {
    private static Map<BankNote, Integer> ATM_BALANCE_MAP_SIMPLE = new HashMap<>();
    private static int SIMPLE_BALANCE = 1500;
    private static Map<BankNote, Integer> ATM_BALANCE_MAP_MISSING_BANKNOTES = new HashMap<>();
    private static int MISSING_BANKNOTE_BALANCE = 600;

    @Mock
    BankRepository bankRepository;
    @Mock
    Bank bank;
    ATMService underTest;

    @BeforeEach
    void beforeEach() {

        underTest = new ATMServiceImpl(bankRepository);
        lenient().doReturn(bank).when(bankRepository).findBankByNameEquals(anyString());


    }

    @Test
    void givenBankEntityWithBalanceMap_whenGetATMBalance_thenReturnCorrectBalance() {
        //Arrange
        BankNote bankNote5 = new BankNote();
        bankNote5.setAmount(5);
        BankNote bankNote10 = new BankNote();
        bankNote10.setAmount(10);
        BankNote bankNote20 = new BankNote();
        bankNote20.setAmount(20);
        BankNote bankNote50 = new BankNote();
        bankNote50.setAmount(50);

        ATM_BALANCE_MAP_SIMPLE.put(bankNote5, 20);
        ATM_BALANCE_MAP_SIMPLE.put(bankNote10, 30);
        ATM_BALANCE_MAP_SIMPLE.put(bankNote20, 30);
        ATM_BALANCE_MAP_SIMPLE.put(bankNote50, 10);
        doReturn(ATM_BALANCE_MAP_SIMPLE).when(bank).getBalanceMap();
        //Act
        final Integer atmBalance = underTest.getATMBalance();
        // Assert
        assertEquals(SIMPLE_BALANCE, atmBalance);
    }

    @Test
    void givenBankEntityWithBalanceMapMissingBankNotes_whenGetATMBalance_thenReturnCorrectBalance() {
        //Arrange
        BankNote bankNote5 = new BankNote();
        bankNote5.setAmount(5);
        BankNote bankNote50 = new BankNote();
        bankNote50.setAmount(50);

        ATM_BALANCE_MAP_MISSING_BANKNOTES.put(bankNote5, 20);
        ATM_BALANCE_MAP_MISSING_BANKNOTES.put(bankNote50, 10);
        doReturn(ATM_BALANCE_MAP_MISSING_BANKNOTES).when(bank).getBalanceMap();
        //Act
        final Integer atmBalance = underTest.getATMBalance();
        // Assert
        assertEquals(MISSING_BANKNOTE_BALANCE, atmBalance);
    }
}
