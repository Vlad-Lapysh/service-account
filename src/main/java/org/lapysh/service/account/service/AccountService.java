package org.lapysh.service.account.service;

import org.lapysh.service.account.data.entity.AccountEntity;
import org.lapysh.service.account.data.repository.AccountRepository;
import org.lapysh.service.account.model.account.balance.AccountBalanceChangeRequest;
import org.lapysh.service.account.model.account.balance.AccountBalanceResponse;
import org.lapysh.service.account.model.account.create.AccountCreateRequest;
import org.lapysh.service.account.model.account.create.AccountCreateResponse;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;

@Service
public class AccountService {

    private final AccountRepository accountRepository;

    public AccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    public AccountCreateResponse create(AccountCreateRequest request) {
        if (request.getAccountBalance() == null) {
            request.setAccountBalance(BigDecimal.ZERO);
        }
        AccountEntity account = new AccountEntity().setClientId(request.getClientId())
                                                   .setAccountBalance(request.getAccountBalance());
        String accountId = accountRepository.save(account).getId();
        return new AccountCreateResponse().setAccountId(accountId);
    }

    @Cacheable("balances")
    public AccountBalanceResponse getBalance(String accountId) {
        return accountRepository.findById(accountId)
                                .map(x -> new AccountBalanceResponse().setAccountBalance(x.getAccountBalance()))
                                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
    }

    @CacheEvict(value = "balances", key = "#accountId")
    @Transactional(isolation = Isolation.REPEATABLE_READ)
    public void changeBalance(String accountId, AccountBalanceChangeRequest request) {
        AccountEntity account = accountRepository.findById(accountId)
                                                 .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        var currAccountBalance = account.getAccountBalance();
        var updatedBalance = currAccountBalance.add(request.getAddToAccountBalance());
        if (updatedBalance.compareTo(BigDecimal.ZERO) < 0) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST);
        }
        account.setAccountBalance(updatedBalance);
        accountRepository.save(account);
    }
}
