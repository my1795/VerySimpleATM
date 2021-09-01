package com.neueda.atm.controller;

import com.neueda.atm.common.constant.APIPathValues;
import com.neueda.atm.common.constant.RequestType;
import com.neueda.atm.resource.AccountRequest;
import com.neueda.atm.resource.AccountResponse;
import com.neueda.atm.service.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(APIPathValues.REQUEST_ACCOUNTS_BASE_URL)
public class AccountController {

    AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ApiOperation(value = "Check Balance of an account" , response = AccountResponse.class)
    @RequestMapping(path = "/queryBalance" , method = RequestMethod.GET)
    public ResponseEntity<AccountResponse> checkBalance(
            @ApiParam(value = "Account Number", required = true) @NotNull @PathVariable("accountNumber") @NotBlank Long accountNumber,
            @ApiParam("Pin of the user.") @NotBlank @NotNull  @RequestParam(value = "pin") Integer pin

    ){
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(accountNumber);
        accountRequest.setRequestType(RequestType.BALANCE_CHECK);
        accountRequest.setPin(pin);
        AccountResponse accountResponse = accountService.checkBalance(accountRequest);
        return new ResponseEntity<AccountResponse>(accountResponse, HttpStatus.OK);
    }
    @ApiOperation(value = "Withdraw from an account an account" , response = AccountResponse.class)
    @RequestMapping(path = "/withdraw" , method = RequestMethod.POST)
    public ResponseEntity<AccountResponse> withdraw(
            @ApiParam(value = "Withdraw request resource")
            @RequestBody(required = true) @NotBlank  @NotNull AccountRequest accountRequest

    ) throws Exception {
        AccountResponse accountResponse = accountService.withdraw(accountRequest);
        return new ResponseEntity<AccountResponse>(accountResponse, HttpStatus.ACCEPTED);
    }
}