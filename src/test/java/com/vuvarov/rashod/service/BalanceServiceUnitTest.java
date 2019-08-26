package com.vuvarov.rashod.service;


import com.vuvarov.rashod.RashodApplication;
import com.vuvarov.rashod.model.dto.AccountBalance;
import com.vuvarov.rashod.repository.AccountRepository;
import org.junit.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.math.BigDecimal;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.CoreMatchers.notNullValue;
import static org.junit.Assert.assertThat;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = {RashodApplication.class})
@ActiveProfiles("test")
public class BalanceServiceUnitTest {

    @Autowired
    private BalanceService balanceService;
    @MockBean
    private AccountRepository accountRepository;
    @MockBean
    private OperationService operationService;

//    @Before
//    public void setup(){
//        ArrayList<Operation> operations = new ArrayList<>();
//        Operation operation = new Operation();
//        BDDMockito.given(operationService.findAllOperations(eq(2L),eq(false)))
//        .willReturn(operations);
//    }

    @ParameterizedTest(name = "Text {index} : ''{0}'' ''{1}''")
    @ValueSource(longs = {1L, 2L})
    @Test
    public void testCalcBalance(Long accountId) {
        AccountBalance balance = balanceService.calculate(accountId);
        assertThat("balance can't be null", balance, notNullValue());
        assertThat("account id can be equals is called accountId", balance.getAccountId(), is(accountId));
        assertThat("balance can be equals ZERO", balance.getBalance(), is(BigDecimal.ZERO));
    }
}
