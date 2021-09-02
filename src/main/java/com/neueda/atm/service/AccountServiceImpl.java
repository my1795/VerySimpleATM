package com.neueda.atm.service;

import com.google.common.base.Preconditions;
import com.neueda.atm.common.constant.RequestType;
import com.neueda.atm.entity.AccountEntity;
import com.neueda.atm.entity.Bank;
import com.neueda.atm.entity.BankNote;
import com.neueda.atm.repository.AccountRepository;
import com.neueda.atm.repository.BankRepository;
import com.neueda.atm.resource.AccountRequest;
import com.neueda.atm.resource.AccountResponse;
import com.neueda.atm.resource.BankNoteResource;
import com.neueda.atm.util.BalanceUtil;
import org.apache.commons.lang3.NotImplementedException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AccountServiceImpl implements AccountService {
    private static final Logger logger = LogManager.getLogger(AccountServiceImpl.class);
    private static String REQUEST_TYPE_ERROR = "Incompatible Request Type";
    AccountRepository accountRepository;

    BankRepository bankRepository;

    ATMService atmService;

    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository, ATMService atmService) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
        this.atmService = atmService;
    }

    @Override
    public AccountResponse checkBalance(AccountRequest accountRequest) {
        accountRequest.getRequestType().equals(RequestType.BALANCE_CHECK);
        Preconditions.checkArgument(accountRequest.getRequestType().equals(RequestType.BALANCE_CHECK), REQUEST_TYPE_ERROR);
        AccountEntity accountEntity = accountRepository.findAccountEntityByAccountNumberEquals(accountRequest.getAccountNumber());
        validateBalanceCheckRequest(accountRequest);
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountNumber(accountEntity.getAccountNumber());
        accountResponse.setRequestType(RequestType.BALANCE_CHECK);
        accountResponse.setBalance(accountEntity.getBalance());
        accountResponse.setOverdraft(accountEntity.getOverdraft());
        accountResponse.setMaximumWithdrawalAmount(accountEntity.getBalance()+accountEntity.getOverdraft());
        return accountResponse;
    }

    @Override
    @Transactional
    public AccountResponse withdraw(AccountRequest accountRequest) throws Exception {
        Preconditions.checkArgument(accountRequest.getRequestType().equals(RequestType.WITHDRAW), REQUEST_TYPE_ERROR);
        validateWithdrawRequest(accountRequest);
        AccountResponse accountResponse = new AccountResponse();
        synchronized (this) {
            AtomicInteger withdrawAmount = new AtomicInteger();
            Bank bank = bankRepository.findBankByNameEquals("atm1");
            AccountEntity accountEntity = accountRepository.findAccountEntityByAccountNumberEquals(accountRequest.getAccountNumber());
            Map<BankNote, Integer> bankBalance = bank.getBalanceMap();
            Map<BankNote, Integer> withdrawAmountMap = BalanceUtil.getMinBankNotesByWithdrawAmount(bankBalance, accountRequest.getRequestAmount());
            withdrawAmountMap.forEach((bankNote, count) -> {
                if (bankBalance.get(bankNote) == count) {
                    int bankNoteCount = bankBalance.get(bankNote);
                    bankBalance.remove(bankNote);
                    withdrawAmount.getAndAdd(bankNoteCount * bankNote.getAmount());
                } else {
                    int currentCount = bankBalance.get(bankNote);
                    bankBalance.replace(bankNote, currentCount - count);
                    withdrawAmount.getAndAdd(count * bankNote.getAmount());
                }
            });
            bank.setBalanceMap(bankBalance);
            if (accountEntity.getBalance() >= withdrawAmount.get()) {
                accountEntity.setBalance(accountEntity.getBalance() - withdrawAmount.get());
            } else {
                double deductedFromOverdraft = withdrawAmount.get() - accountEntity.getBalance();
                accountEntity.setBalance(0);
                accountEntity.setOverdraft(accountEntity.getOverdraft() - deductedFromOverdraft);
            }


            accountResponse.setRemainingBalance(accountEntity.getBalance());
            accountResponse.setOverdraft(accountEntity.getOverdraft());
            accountResponse.setRequestType(RequestType.WITHDRAW);
            accountResponse.setAccountNumber(accountEntity.getAccountNumber());
            accountResponse.setWithdrawalBankNotes(new ArrayList<>());
            withdrawAmountMap.forEach(((bankNote, count) -> {
                BankNoteResource bankNoteResource = new BankNoteResource();
                bankNoteResource.setBankNoteAmount(bankNote.getAmount());
                bankNoteResource.setBankNoteCount(count);
                accountResponse.getWithdrawalBankNotes().add(bankNoteResource);
            }));
        }
        return accountResponse;
    }

    @Override
    public AccountResponse deposit(AccountRequest accountRequest) {
        throw new NotImplementedException();
    }

    private void validateBalanceCheckRequest(AccountRequest accountRequest) {
        AccountEntity accountEntity = accountRepository.findAccountEntityByAccountNumberEquals(accountRequest.getAccountNumber());

        basicAccountResourceCheck(accountRequest, accountEntity);

    }

    private void validateWithdrawRequest(AccountRequest accountRequest) {
        AccountEntity accountEntity = accountRepository.findAccountEntityByAccountNumberEquals(accountRequest.getAccountNumber());

        basicAccountResourceCheck(accountRequest, accountEntity);

        if (accountRequest.getRequestAmount() > atmService.getATMBalance()) {
            logger.error("Withdraw request rejected. ATM balance : {} account withdraw request : {} accountNumber : {}", atmService.getATMBalance(), accountRequest.getRequestAmount(),
                    accountRequest.getAccountNumber());
            throw new UnsupportedOperationException("Withdraw amount cannot exceed atm balance.");
        }
        if (accountRequest.getRequestAmount() > accountEntity.getBalance() + accountEntity.getOverdraft()) {
            logger.error("Withdraw request rejected.Account Balance + overdraft is not enough Account balance : {} account overdraft : {} " +
                            "account withdraw request : {} accountNumber : {}", accountEntity.getBalance(),
                    accountEntity.getOverdraft(), accountRequest.getRequestAmount(), accountRequest.getAccountNumber());
            throw new UnsupportedOperationException("Account does not have enough credits");
        }

    }

    private void basicAccountResourceCheck(AccountRequest accountRequest, AccountEntity accountEntity) throws EntityNotFoundException, SecurityException {
        if (accountEntity == null) {
            logger.error("Account entity is not found by accountNumber : {}", accountRequest.getAccountNumber());
            throw new EntityNotFoundException("Account entity is not found with specified attributes");
        }
        if (accountEntity.getPin() != accountRequest.getPin()) {
            logger.error("Incorrect pin is provided for accountNumber : {}", accountRequest.getAccountNumber());
            throw new SecurityException("Given pin does not match with the account");
        }
    }
}
