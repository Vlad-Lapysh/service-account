package org.lapysh.service.account.model.account.create;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;
import lombok.experimental.Accessors;

import static com.fasterxml.jackson.annotation.JsonInclude.Include.NON_NULL;

@Data
@Accessors(chain = true)
@JsonInclude(NON_NULL)
public class AccountCreateResponse {
    String accountId;
}
