package com.nttbootcamp.bootcoin.infraestructure.interfaces;

import com.nttbootcamp.bootcoin.domain.beans.BootCoinOperationDTO;
import com.nttbootcamp.bootcoin.domain.model.Transaction;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootCoinAccountTransactionService {
	
	Flux<Transaction> findAll();
	
	Mono<Transaction> delete(String id);

	Mono<Transaction> findById(String id);
	
	Mono<ResponseEntity<Transaction>> update(String id, Transaction request);
	
	Flux<Transaction> saveAll(List<Transaction> a);

    Mono<Transaction> doBootCoinPayment(BootCoinOperationDTO dto);

}
