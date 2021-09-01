package com.neueda.atm.service;

import com.neueda.atm.common.constant.RequestType;
import com.neueda.atm.entity.AccountEntity;
import com.neueda.atm.entity.Bank;
import com.neueda.atm.entity.BankNote;
import com.neueda.atm.repository.AccountRepository;
import com.neueda.atm.repository.BankRepository;
import com.neueda.atm.resource.AccountRequest;
import com.neueda.atm.resource.AccountResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import javax.persistence.EntityNotFoundException;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@Tag("unit")
public class AccountServiceImplUT {
    private static long NON_EXISTING_ACCOUNT_NUMBER = 0l;
    private static long EXISTING_ACCOUNT_NUMBER = 1l;

    private static int CORRECT_PIN = 1234;

    private static int WRONG_PIN = 253;
    private static String BANK_NAME="atm1";

    private static double VALID_ACCOUNT_BALANCE = 800;

    private static int WRONG_ACCOUNT_BALANCE = 800;

    private static double VALID_OVERDRAFT_BALANCE = 200;

    private static int VALID_REQUEST_AMOUNT = 200;

    private static int VALID_ATM_BALANCE = 1200;

    private static int EXACT_BANKNOTE_AMOUNT = 5;
    private static Map< BankNote, Integer> VALID_ATM_BALANCE_MAP =  new HashMap<>();

    private static Map< BankNote, Integer> EXACT_BANKNOTE_COUNT_MAP =  new HashMap<>();
    @Mock
    AccountRepository accountRepository;
    @Mock
    BankRepository bankRepository;
    @Mock
    ATMService atmService;
    @Mock
    Bank bank;
    @Mock
    AccountEntity accountEntity;
    AccountServiceImpl underTest;

    @BeforeEach
    void beforeEach() {

        underTest = new AccountServiceImpl(accountRepository,bankRepository,atmService);
        lenient().doReturn(bank).when(bankRepository).findBankByNameEquals(BANK_NAME);
        lenient().doReturn(null).when(accountRepository).findAccountEntityByAccountNumberEquals(NON_EXISTING_ACCOUNT_NUMBER);
        lenient().doReturn(accountEntity).when(accountRepository).findAccountEntityByAccountNumberEquals(EXISTING_ACCOUNT_NUMBER);
        lenient().doReturn(EXISTING_ACCOUNT_NUMBER).when(accountEntity).getAccountNumber();
        lenient().doReturn(CORRECT_PIN).when(accountEntity).getPin();
        lenient().doReturn(VALID_ACCOUNT_BALANCE).when(accountEntity).getBalance();
        lenient().doReturn(VALID_OVERDRAFT_BALANCE).when(accountEntity).getOverdraft();

        BankNote bankNote5 = new BankNote();
        bankNote5.setAmount(5);
        BankNote bankNote10 = new BankNote();
        bankNote10.setAmount(10);
        BankNote bankNote20 = new BankNote();
        bankNote20.setAmount(10);
        BankNote bankNote50 = new BankNote();
        bankNote50.setAmount(50);

        VALID_ATM_BALANCE_MAP.put(bankNote5, 20);
        VALID_ATM_BALANCE_MAP.put(bankNote10, 30);
        VALID_ATM_BALANCE_MAP.put(bankNote20, 30);
        VALID_ATM_BALANCE_MAP.put(bankNote50, 10);
        lenient().doReturn(VALID_ATM_BALANCE_MAP).when(bank).getBalanceMap();
        lenient().doReturn(VALID_ATM_BALANCE).when(atmService).getATMBalance();

    }
    @Test
    void givenNonExistingAccountNumber_whenCheckBalance_thenThrowEntityNotFoundException() {
        //Arrange
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(123456789l);
        accountRequest.setRequestType(RequestType.BALANCE_CHECK);
        //Act
        final Executable executable = () -> underTest.checkBalance(accountRequest);
        // Assert
        assertThrows(EntityNotFoundException.class, executable);
    }
    @Test
    void givenWrongPin_whenCheckBalance_thenThrowSecurityException() {
        //Arrange
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(EXISTING_ACCOUNT_NUMBER);
        accountRequest.setRequestType(RequestType.BALANCE_CHECK);
        accountRequest.setPin(WRONG_PIN);
        //Act
        final Executable executable = () -> underTest.checkBalance(accountRequest);
        // Assert
        assertThrows(SecurityException.class, executable);
    }
    @Test
    void givenIncompatibleRequestType_whenCheckBalance_thenThrowIllegalArgumentException() {
        //Arrange
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(EXISTING_ACCOUNT_NUMBER);
        accountRequest.setPin(CORRECT_PIN);
        accountRequest.setRequestType(RequestType.DEPOSIT);
        //Act
        final Executable executable = () -> underTest.checkBalance(accountRequest);
        // Assert
        assertThrows(IllegalArgumentException.class, executable);
    }
    @Test
    void givenCorrectAccountRequest_whenCheckBalance_thenReturnExpectedAccountResource() {
        //Arrange
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(EXISTING_ACCOUNT_NUMBER);
        accountRequest.setPin(CORRECT_PIN);
        accountRequest.setRequestType(RequestType.BALANCE_CHECK);
        //Act
        final AccountResponse accountResponse =underTest.checkBalance(accountRequest);
        // Assert
        assertEquals(accountResponse.getRequestType(), accountRequest.getRequestType());
        assertEquals(accountResponse.getAccountNumber(), accountRequest.getAccountNumber());
        assertEquals(accountResponse.getBalance(), VALID_ACCOUNT_BALANCE);
    }
    @Test
    void givenNonExistingAccountNumber_whenWithdraw_thenThrowEntityNotFoundException() {
        //Arrange
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(123456789l);
        accountRequest.setRequestType(RequestType.WITHDRAW);
        //Act
        final Executable executable = () -> underTest.withdraw(accountRequest);
        // Assert
        assertThrows(EntityNotFoundException.class, executable);
    }
    @Test
    void givenWrongPin_whenWithdraw_thenThrowSecurityException() {
        //Arrange
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(EXISTING_ACCOUNT_NUMBER);
        accountRequest.setRequestType(RequestType.WITHDRAW);
        accountRequest.setPin(WRONG_PIN);
        //Act
        final Executable executable = () -> underTest.withdraw(accountRequest);
        // Assert
        assertThrows(SecurityException.class, executable);
    }
    @Test
    void givenIncompatibleRequestType_whenWithdraw_thenThrowIllegalArgumentException() {
        //Arrange
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(EXISTING_ACCOUNT_NUMBER);
        accountRequest.setPin(CORRECT_PIN);
        accountRequest.setRequestType(RequestType.DEPOSIT);
        //Act
        final Executable executable = () -> underTest.withdraw(accountRequest);
        // Assert
        assertThrows(IllegalArgumentException.class, executable);
    }
    @Test
    void givenCorrectAccountRequest_whenWithdraw_thenReturnExpectedAccountResource() throws Exception {
        //Arrange
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(EXISTING_ACCOUNT_NUMBER);
        accountRequest.setPin(CORRECT_PIN);
        accountRequest.setRequestType(RequestType.WITHDRAW);
        accountRequest.setRequestAmount(VALID_REQUEST_AMOUNT);
        int initialAtmBalance = atmService.getATMBalance();
        //Act
        final AccountResponse accountResponse =underTest.withdraw(accountRequest);
        // Assert
        assertEquals(accountResponse.getRequestType(), accountRequest.getRequestType());
        assertEquals(accountResponse.getAccountNumber(), accountRequest.getAccountNumber());
        //assertEquals(VALID_ACCOUNT_BALANCE - VALID_REQUEST_AMOUNT , accountResponse.getRemainingBalance());
        //assertEquals(atmService.getATMBalance(), initialAtmBalance - VALID_REQUEST_AMOUNT);
    }
    @Test
    void givenCorrectAccountRequest_whenWithdrawIsEqualsExactBankNoteCount_thenReturnExpectedAccountResource() throws Exception {
        //Arrange
        BankNote bankNote5 = new BankNote();
        bankNote5.setAmount(5);
        EXACT_BANKNOTE_COUNT_MAP.put(bankNote5, 1);
        doReturn(EXACT_BANKNOTE_COUNT_MAP).when(bank).getBalanceMap();

        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(EXISTING_ACCOUNT_NUMBER);
        accountRequest.setPin(CORRECT_PIN);
        accountRequest.setRequestType(RequestType.WITHDRAW);
        accountRequest.setRequestAmount(EXACT_BANKNOTE_AMOUNT);
        int initialAtmBalance = atmService.getATMBalance();
        //Act
        final AccountResponse accountResponse =underTest.withdraw(accountRequest);
        // Assert
        assertEquals(accountResponse.getRequestType(), accountRequest.getRequestType());
        assertEquals(accountResponse.getAccountNumber(), accountRequest.getAccountNumber());
        //assertEquals(VALID_ACCOUNT_BALANCE - VALID_REQUEST_AMOUNT , accountResponse.getRemainingBalance());
        //assertEquals(atmService.getATMBalance(), initialAtmBalance - VALID_REQUEST_AMOUNT);
    }
}
