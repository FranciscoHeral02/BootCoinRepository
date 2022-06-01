package com.nttbootcamp.bootcoin.infraestructure.interfaces;

import com.nttbootcamp.bootcoin.domain.beans.BootCoinExchangeDTO;
import com.nttbootcamp.bootcoin.domain.model.BootCoinExchange;
import com.nttbootcamp.bootcoin.domain.model.Transaction;
import org.springframework.http.ResponseEntity;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;

public interface IBootCoinExchangeService {

    Flux<BootCoinExchange> findAll();

    Mono<BootCoinExchange> delete(String id);

    Mono<BootCoinExchange> findById(String id);

    Mono<BootCoinExchange> update(String id, Transaction request);

    Mono<BootCoinExchange> save(BootCoinExchangeDTO dto);
}
