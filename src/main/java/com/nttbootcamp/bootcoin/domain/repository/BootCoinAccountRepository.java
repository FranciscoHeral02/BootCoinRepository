package com.nttbootcamp.bootcoin.domain.repository;


import com.nttbootcamp.bootcoin.domain.model.BootCoinAccount;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import reactor.core.publisher.Mono;

public interface BootCoinAccountRepository extends ReactiveMongoRepository<BootCoinAccount, String> {
	Mono<Boolean> existsByCellphoneNumber(String cellphoneNumber);
	Mono<BootCoinAccount> findByLinkedAccount(String linkedAccount);
}
