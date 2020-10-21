package org.lapysh.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.lapysh.service.account.ServiceAccountApplication;
import org.lapysh.service.account.data.repository.AccountRepository;
import org.lapysh.service.account.model.account.create.AccountCreateRequest;
import org.lapysh.service.account.model.account.create.AccountCreateResponse;
import org.lapysh.service.account.service.AccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import static java.text.MessageFormat.format;
import static org.junit.Assert.assertEquals;
import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;

@ActiveProfiles(profiles = "test-race")
@SpringBootTest(classes = TestConfig.class)
@AutoConfigureMockMvc
@ContextConfiguration(classes = ServiceAccountApplication.class)
public class RaceConditionTests {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private AccountService accountService;
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ObjectMapper mapper;

    @Test
    public void withdrawalInRace() throws Exception {
        AccountCreateResponse account = accountService.create(new AccountCreateRequest("1", BigDecimal.valueOf(100)));

        assertEquals(100, accountRepository.findById(account.getAccountId()).get().getAccountBalance().intValue());

        Random random = new Random();
        List<Thread> tlist = new ArrayList<>(100);
        for (int i = 0; i < 40; i++) {
            final  int x = i;
            Thread t = new Thread(() -> {
                try {
                    Thread.sleep(random.nextInt(100) + 1);
                    String result = mockMvc.perform(patch(format("/v1/accounts/{0}/balance", account.getAccountId()))
                            .contentType(APPLICATION_JSON)
                            .content("{\"addToAccountBalance\": -3.0}"))
//                            .andExpect(status().isNoContent())
                            .andReturn().getResponse().getContentAsString();

                    System.out.println("Result: " + x +" : " + result);
                } catch (Exception e) {
                    System.out.println(e.getLocalizedMessage());
                }
            });
            t.start();
//            t.join();
            tlist.add(t);
        }
        for (Thread thread : tlist) {
            thread.join();
        }

        assertEquals(1, accountRepository.findById(account.getAccountId()).get().getAccountBalance().intValue());
    }

}
