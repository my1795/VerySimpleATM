package com.neueda.atm.controller;

import com.neueda.atm.common.constant.APIPathValues;
import com.neueda.atm.common.constant.RequestType;
import com.neueda.atm.resource.AccountRequest;
import com.neueda.atm.resource.AccountRequestWrapper;
import com.neueda.atm.resource.AccountResponse;
import com.neueda.atm.service.AccountService;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@RestController
@RequestMapping(APIPathValues.REQUEST_ACCOUNTS_BASE_URL)
public class AccountController {
    private static final Logger logger = LogManager.getLogger(AccountController.class);

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
        logger.trace("Balance Check Request received for accountNumber: {} ",accountNumber);
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(accountNumber);
        accountRequest.setRequestType(RequestType.BALANCE_CHECK);
        accountRequest.setPin(pin);
        AccountResponse accountResponse = accountService.checkBalance(accountRequest);
        logger.trace("Balance Check Request completed for accountNumber: {} returning response :{}",accountNumber,accountResponse);
        return new ResponseEntity<AccountResponse>(accountResponse, HttpStatus.OK);
    }
    @ApiOperation(value = "Withdraw from an account an account" , response = AccountResponse.class)
    @RequestMapping(path = "/withdraw" , method = RequestMethod.POST)
    public ResponseEntity<AccountResponse> withdraw(
            @ApiParam(value = "Account Number", required = true) @NotNull @PathVariable("accountNumber") @NotBlank Long accountNumber,
            @ApiParam(value = "Withdraw request resource")
            @RequestBody(required = true) @NotBlank  @NotNull AccountRequestWrapper accountRequestWrapper

    ) throws Exception {
        AccountRequest accountRequest = new AccountRequest();
        accountRequest.setAccountNumber(accountNumber);
        accountRequest.setPin(accountRequestWrapper.getPin());
        accountRequest.setRequestType(RequestType.WITHDRAW);
        accountRequest.setRequestAmount(accountRequestWrapper.getRequestAmount());
        logger.trace("Withdraw Request received for accountNumber: {} ",accountRequest.getAccountNumber());
        AccountResponse accountResponse = accountService.withdraw(accountRequest);
        logger.trace("Withdraw Request completed for accountNumber: {} returning response :{}",accountRequest.getAccountNumber(),accountResponse);
        return new ResponseEntity<AccountResponse>(accountResponse, HttpStatus.ACCEPTED);
    }
}