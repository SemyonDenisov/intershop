package ru.yandex.payment.dao;

import org.springframework.data.r2dbc.repository.R2dbcRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;
import reactor.util.annotation.NonNull;
import ru.yandex.payment.model.Balance;


@Repository
public interface BalanceRepository extends R2dbcRepository<Balance, Integer> {
    @NonNull
    Mono<Balance> findByUserId(@NonNull Integer userId);
}
