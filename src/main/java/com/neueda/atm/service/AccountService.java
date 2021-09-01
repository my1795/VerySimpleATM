package com.neueda.atm.service;

import com.neueda.atm.resource.AccountRequest;
import com.neueda.atm.resource.AccountResponse;
import org.springframework.stereotype.Service;


public interface AccountService {

    public  AccountResponse checkBalance(AccountRequest accountRequest);
    public  AccountResponse withdraw(AccountRequest accountRequest) throws Exception;
    public  AccountResponse deposit(AccountRequest accountRequest);
}
