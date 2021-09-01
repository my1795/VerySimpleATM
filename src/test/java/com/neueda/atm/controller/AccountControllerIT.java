package com.neueda.atm.controller;

import com.neueda.atm.common.constant.RequestType;
import com.neueda.atm.config.SpringBootTestConfig;
import com.neueda.atm.service.AccountService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mockito.Mock;

import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTestConfig
public class AccountControllerIT {

    private static long NON_EXISTING_ACCOUNT_NUMBER = 0l;
    private static long EXISTING_ACCOUNT_NUMBER = 1l;

    private static int CORRECT_PIN = 1234;

    @Mock
    AccountService accountService;
    AccountController underTest;

    @BeforeEach
    void beforeEach() {

        underTest = new AccountController(accountService);


    }

    @Test
    void givenEmptyAccountNumberPathVariable_whenCheckBalance_thenThrowException() {
        //Arrange
        //Act
        final Executable executable = () -> underTest.checkBalance(null, CORRECT_PIN, RequestType.BALANCE_CHECK);
        // Assert
        assertThrows(Exception.class, executable);
    }

}
