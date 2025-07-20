package ru.yandex.payment.unit.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import reactor.core.publisher.Mono;
import ru.yandex.payment.dao.BalanceRepository;
import ru.yandex.payment.model.Action;
import ru.yandex.payment.model.Balance;
import ru.yandex.payment.model.CartPaymentResponse;
import ru.yandex.payment.service.BalanceService;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BalanceServiceUnitTests {
    @Autowired
    BalanceService balanceService;

    @MockitoBean
    BalanceRepository balanceRepository;

    @BeforeEach
    void setUp() {
        reset(balanceRepository);
    }

    @Test
    void test_getBalance(){
        when(balanceRepository.findByUserId(1)).thenReturn(Mono.just(new Balance(5.0,1)));
        double balance = balanceService.getBalanceByUserId(1).block();
        verify(balanceRepository, times(1)).findByUserId(1);
        assertEquals(5.0,balance);
    }

    @Test
    void test_payment(){
        when(balanceRepository.findByUserId(1)).thenReturn(Mono.just(new Balance(5.0,1)));
        when(balanceRepository.save(any(Balance.class))).thenReturn(Mono.just(new Balance(2.0,1)));
        CartPaymentResponse cr = balanceService.changeBalance(1,3.0, Action.MINUS).block();
        verify(balanceRepository, times(1)).findByUserId(1);
        assertEquals(cr.getNewBalance(),2.0);
    }

}
