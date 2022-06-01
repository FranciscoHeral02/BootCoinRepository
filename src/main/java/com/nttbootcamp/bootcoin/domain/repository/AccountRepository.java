package com.nttbootcamp.bootcoin.domain.repository;

import com.nttbootcamp.bootcoin.domain.model.Account;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface AccountRepository extends ReactiveMongoRepository<Account, String> {
    Mono<Boolean> existsByAccountNumberAndBusinessPartnerId(String accountNumber,String bdPartnerId);

    Mono<Account> findByAccountNumberAndBusinessPartnerId(String debitCardNumber, String docNum);
}
