package com.neueda.atm.service;

import com.neueda.atm.resource.AccountRequest;
import com.neueda.atm.resource.AccountResponse;
import org.springframework.stereotype.Service;


public interface AccountService {

    public  AccountResponse getBalance(AccountRequest accountRequest);
    public  AccountResponse withdraw(AccountRequest accountRequest);
    public  AccountResponse deposit(AccountRequest accountRequest);
}
