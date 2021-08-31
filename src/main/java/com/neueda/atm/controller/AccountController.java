package com.neueda.atm.controller;

import com.neueda.atm.common.constant.APIPathValues;
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
import java.io.UnsupportedEncodingException;
@RestController
@RequestMapping(APIPathValues.REQUEST_ACCOUNTS_BASE_URL)
public class AccountController {

    AccountService accountService;

    @Autowired
    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @ApiOperation(value = "Check Balance of an account" , response = AccountResponse.class)
    @RequestMapping(path = "/" , method = RequestMethod.GET)
    public ResponseEntity<AccountResponse> getBalance(
            @ApiParam(value = "Account Number", required = true) @NotBlank @PathVariable("accountNumber") long accountNumber,
            @ApiParam("Pin of the user.") @RequestParam(value = "pin", required = false) String pin,
            @ApiParam("Request Type.") @RequestParam(value = "requestType", required = false) String requestType

    ){
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(accountNumber);
        accountRequest.setRequestType(requestType);
        AccountResponse accountResponse = accountService.getBalance(accountRequest);
        return new ResponseEntity<AccountResponse>(accountResponse, HttpStatus.OK);
    }
    @ApiOperation(value = "Withdraw from an account an account" , response = AccountResponse.class)
    @RequestMapping(path = "/withdraw" , method = RequestMethod.POST)
    public ResponseEntity<AccountResponse> withdraw(
            @ApiParam(value = "Account Number", required = true) @NotBlank @PathVariable("accountNumber") long accountNumber,
            @ApiParam(value = "Withdraw reuqest resource")
            @RequestBody(required = true) AccountRequest accountRequest

    ){
        AccountResponse accountResponse = accountService.withdraw(accountRequest);
        return new ResponseEntity<AccountResponse>(accountResponse, HttpStatus.OK);
    }
}