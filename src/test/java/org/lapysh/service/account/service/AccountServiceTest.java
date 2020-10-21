package org.lapysh.service.account.service;

import org.junit.jupiter.api.Test;
import org.lapysh.service.account.data.entity.AccountEntity;
import org.lapysh.service.account.data.repository.AccountRepository;
import org.lapysh.service.account.model.account.balance.AccountBalanceChangeRequest;
import org.lapysh.service.account.model.account.create.AccountCreateRequest;
import org.lapysh.service.account.model.account.create.AccountCreateResponse;
import org.mockito.ArgumentCaptor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;
import org.springframework.web.server.ResponseStatusException;

import java.math.BigDecimal;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;

@ActiveProfiles(profiles = "test")
@SpringBootTest
@SpringJUnitConfig
public class AccountServiceTest {

    public static final String ACCOUNT_ID = "2d89c54a-d00c-4842-816b-20141dd53cac";
    public static final String CLIENT_ID = "954d4b23-5096-41cd-91a1-c704054984ef";

    @MockBean
    AccountRepository accountRepository;

    @Autowired
    AccountService accountService;

    @Test
    public void create_whenValidData_thenAccountShouldBeCreated() {
        BigDecimal accountBalance = BigDecimal.valueOf(123.45);
        AccountEntity account = new AccountEntity().setId(ACCOUNT_ID)
                                                   .setClientId(CLIENT_ID)
                                                   .setAccountBalance(accountBalance);
        Mockito.when(accountRepository.save(any(AccountEntity.class))).thenReturn(account);
        AccountCreateRequest request = new AccountCreateRequest().setAccountBalance(accountBalance)
                                                                 .setClientId(CLIENT_ID);

        AccountCreateResponse createResponse = accountService.create(request);

        assertThat(createResponse.getAccountId()).isEqualTo(ACCOUNT_ID);
    }

    @Test
    void getBalance_whenValidAccountId_thenBalanceShouldBeReceived() {
        BigDecimal accountBalance = BigDecimal.valueOf(123.45);
        AccountEntity account = new AccountEntity().setId(ACCOUNT_ID)
                                                   .setClientId(CLIENT_ID)
                                                   .setAccountBalance(accountBalance);
        Mockito.when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));

        var balanceResponse = accountService.getBalance(ACCOUNT_ID);

        assertThat(balanceResponse.getAccountBalance()).isEqualTo(accountBalance);
    }

    @Test
    void changeBalance_whenAddToAccount_thenNoExceptionShouldBe() {
        BigDecimal accountBalance = BigDecimal.valueOf(123.45);
        AccountEntity account = new AccountEntity().setId(ACCOUNT_ID)
                                                   .setClientId(CLIENT_ID)
                                                   .setAccountBalance(accountBalance);
        Mockito.when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        var request = new AccountBalanceChangeRequest().setAddToAccountBalance(BigDecimal.TEN);

        accountService.changeBalance(ACCOUNT_ID, request);

        var argumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(accountRepository).save(argumentCaptor.capture());
        assertThat(accountBalance.add(BigDecimal.ONE)).isNotEqualTo(argumentCaptor.getValue().getAccountBalance());
        assertThat(accountBalance.add(BigDecimal.TEN)).isEqualTo(argumentCaptor.getValue().getAccountBalance());
    }

    @Test
    void changeBalance_whenValidSubtractFromAccount_thenNoExceptionShouldBe() {
        BigDecimal accountBalance = BigDecimal.valueOf(123.45);
        AccountEntity account = new AccountEntity().setId(ACCOUNT_ID)
                                                   .setClientId(CLIENT_ID)
                                                   .setAccountBalance(accountBalance);
        Mockito.when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        BigDecimal subtrahend = BigDecimal.valueOf(-10);
        var request = new AccountBalanceChangeRequest().setAddToAccountBalance(subtrahend);

        accountService.changeBalance(ACCOUNT_ID, request);

        var argumentCaptor = ArgumentCaptor.forClass(AccountEntity.class);
        verify(accountRepository).save(argumentCaptor.capture());
        assertThat(accountBalance.add(BigDecimal.ONE)).isNotEqualTo(argumentCaptor.getValue().getAccountBalance());
        assertThat(accountBalance.add(subtrahend)).isEqualTo(argumentCaptor.getValue().getAccountBalance());
    }

    @Test
    void changeBalance_whenNotValidSubtractFromAccount_thenExceptionShouldBe() {
        BigDecimal accountBalance = BigDecimal.valueOf(123.45);
        AccountEntity account = new AccountEntity().setId(ACCOUNT_ID)
                                                   .setClientId(CLIENT_ID)
                                                   .setAccountBalance(accountBalance);
        Mockito.when(accountRepository.findById(anyString())).thenReturn(Optional.of(account));
        BigDecimal subtrahend = BigDecimal.valueOf(-1000);
        var request = new AccountBalanceChangeRequest().setAddToAccountBalance(subtrahend);

        assertThrows(ResponseStatusException.class, () -> {
            accountService.changeBalance(ACCOUNT_ID, request);
        });
    }
}