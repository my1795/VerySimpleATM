package com.neueda.atm.service;

import com.neueda.atm.entity.AccountEntity;
import com.neueda.atm.entity.Bank;
import com.neueda.atm.entity.BankNote;
import com.neueda.atm.repository.AccountRepository;
import com.neueda.atm.repository.BankRepository;
import com.neueda.atm.resource.AccountRequest;
import com.neueda.atm.resource.AccountResponse;
import com.neueda.atm.util.BalanceUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import javax.transaction.Transactional;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class AccountServiceImpl implements AccountService{

    AccountRepository accountRepository;

    BankRepository bankRepository;

    ATMService atmService;
    @Autowired
    public AccountServiceImpl(AccountRepository accountRepository, BankRepository bankRepository,ATMService atmService) {
        this.accountRepository = accountRepository;
        this.bankRepository = bankRepository;
        this.atmService = atmService;
    }

    @Override
    public AccountResponse getBalance(AccountRequest accountRequest) {

        AccountEntity accountEntity =accountRepository.findAccountEntityByAccountNumberEquals(accountRequest.getAccountNumber());
        basicAccountResourceCheck(accountRequest, accountEntity);
        AccountResponse accountResponse = new AccountResponse();
        accountResponse.setAccountNumber(accountEntity.getAccountNumber());
        accountResponse.setRequestType("CHECK_BALANCE");
        accountResponse.setRemainingBalance(accountEntity.getBalance());
        return accountResponse;
    }

    @Override
    @Transactional
    public AccountResponse withdraw(AccountRequest accountRequest) throws Exception {

        validateWithdrawRequest(accountRequest);

        AccountResponse accountResponse = new AccountResponse();
        AtomicInteger withdrawAmount = new AtomicInteger();
        Bank bank = bankRepository.findBankByNameEquals("atm1");
        AccountEntity accountEntity = accountRepository.findAccountEntityByAccountNumberEquals(accountRequest.getAccountNumber());
        Map<BankNote, Integer> bankBalance =  bank.getBalanceMap();
        Map<BankNote, Integer> withdrawAmountMap = BalanceUtil.getMinBankNotesByWithdrawAmount(bankBalance,accountRequest.getRequestAmount());
        withdrawAmountMap.forEach( (bankNote, count) -> {
            if(bankBalance.get(bankNote) == count){
                int bankNoteCount = bankBalance.get(bankNote);
                bankBalance.remove(bankNote);
                withdrawAmount.getAndAdd(bankNoteCount* bankNote.getAmount());
            }
            else {
                int currentCount = bankBalance.get(bankNote);
                bankBalance.replace(bankNote, currentCount- bankNote.getAmount());
                withdrawAmount.getAndAdd(bankBalance.get(bankNote) * bankNote.getAmount());
            }
        });
        bank.setBalanceMap(bankBalance);

        accountEntity.setBalance(accountEntity.getBalance() - withdrawAmount.get());

        accountResponse.setRemainingBalance(accountEntity.getBalance());
        accountResponse.setRequestType("Withdraw");
        accountResponse.setAccountNumber(accountEntity.getAccountNumber());
        return accountResponse;
    }

    @Override
    public AccountResponse deposit(AccountRequest accountRequest) {
        return null;
    }

    private void validateBalanceCheckRequest(AccountRequest accountRequest){
        AccountEntity accountEntity = accountRepository.findAccountEntityByAccountNumberEquals(accountRequest.getAccountNumber());

        basicAccountResourceCheck(accountRequest, accountEntity);

    }
    private void validateWithdrawRequest(AccountRequest accountRequest){
        AccountEntity accountEntity = accountRepository.findAccountEntityByAccountNumberEquals(accountRequest.getAccountNumber());

        basicAccountResourceCheck(accountRequest, accountEntity);

        if(accountRequest.getRequestAmount() > atmService.getATMBalance()){
            throw new UnsupportedOperationException("Withdraw amount cannot exceed atm balance.");
        }
        if(accountRequest.getRequestAmount() > accountEntity.getBalance() + accountEntity.getOverdraft()){
            throw new UnsupportedOperationException("Account does not have enough credits");
        }

    }
    private void basicAccountResourceCheck(AccountRequest accountRequest, AccountEntity accountEntity) throws EntityNotFoundException,SecurityException{
        if(accountEntity == null){
            throw new EntityNotFoundException("Account entity is not found with specified attributes");
        }
        if(accountEntity.getPin() != accountRequest.getPin()){
            throw new SecurityException("Given pin does not match with the account");
        }
    }
}
