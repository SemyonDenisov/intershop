package ru.yandex.payment.integration.service;

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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@SpringBootTest
public class BalanceServiceIntagrationTests {
    @Autowired
    BalanceService balanceService;

    @Autowired
    BalanceRepository balanceRepository;

    @BeforeEach
    void setUp() {
        balanceRepository.deleteAll().block();
        balanceRepository.save(new Balance(5.0, 1)).block();
    }

    @Test
    void test_getBalance() {
        double balance = balanceService.getBalanceByUserId(1).block();
        assertEquals(5.0, balance);
    }

    @Test
    void test_payment() {
        CartPaymentResponse cr = balanceService.changeBalance(1, 3.0, Action.MINUS).block();
        assertEquals(cr.getNewBalance(), 2.0);
    }

}
