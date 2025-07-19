package ru.yandex.payment.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import ru.yandex.payment.dao.BalanceRepository;
import ru.yandex.payment.model.Action;
import ru.yandex.payment.model.Balance;
import ru.yandex.payment.model.CartPaymentResponse;


@Service
public class BalanceServiceImpl implements BalanceService {

    @Autowired
    BalanceRepository balanceRepository;

    public BalanceServiceImpl(BalanceRepository balanceRepository) {
        this.balanceRepository = balanceRepository;
    }

    @Override
    public Mono<Double> getBalanceByUserId(Integer userId) {
        return balanceRepository.findByUserId(userId).map(Balance::getAmount);
    }

    @Override
    public Mono<CartPaymentResponse> changeBalance(Integer userId, Double amount, Action action) {
        return balanceRepository.findByUserId(userId)
                .flatMap(balance -> {
                    double currentBalance = balance.getAmount();

                    if (action == Action.PLUS) {
                        currentBalance += amount;
                    } else {
                        if (currentBalance < amount) {
                            return Mono.error(new RuntimeException("not enough money"));
                        }
                        currentBalance -= amount;
                    }

                    balance.setAmount(currentBalance);
                    return balanceRepository.save(balance)
                            .map(saved -> new CartPaymentResponse("success", saved.getAmount()));
                });
    }
}
