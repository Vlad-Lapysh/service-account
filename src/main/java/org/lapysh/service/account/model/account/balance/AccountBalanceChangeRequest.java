package org.lapysh.service.account.model.account.balance;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import lombok.experimental.Accessors;

import java.math.BigDecimal;

@Data
@Accessors(chain = true)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountBalanceChangeRequest {
    BigDecimal addToAccountBalance;
}