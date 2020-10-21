package org.lapysh.service.account.controller;

import org.lapysh.service.account.model.account.balance.AccountBalanceChangeRequest;
import org.lapysh.service.account.model.account.balance.AccountBalanceResponse;
import org.lapysh.service.account.model.account.create.AccountCreateRequest;
import org.lapysh.service.account.model.account.create.AccountCreateResponse;
import org.lapysh.service.account.service.AccountService;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@Validated
@RestController
@RequestMapping("/v1/accounts")
public class AccountController {

    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping(consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    public AccountCreateResponse create(@RequestBody @Valid AccountCreateRequest accountCreateRequest) {
        return accountService.create(accountCreateRequest);
    }

    @GetMapping(value = "/{id}/balance", produces = APPLICATION_JSON_VALUE)
    public AccountBalanceResponse getBalance(@PathVariable String id) {
        return accountService.getBalance(id);
    }

    @PatchMapping(value = "/{id}/balance", consumes = APPLICATION_JSON_VALUE, produces = APPLICATION_JSON_VALUE)
    @ResponseStatus(value = HttpStatus.NO_CONTENT)
    public void changeBalance(@PathVariable String id,
                              @RequestBody @Valid AccountBalanceChangeRequest balanceChangeRequest) {
        accountService.changeBalance(id, balanceChangeRequest);
    }

}
