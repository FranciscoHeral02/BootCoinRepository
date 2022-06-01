package com.nttbootcamp.bootcoin.infraestructure.services;

import com.nttbootcamp.bootcoin.application.exception.EntityNotExistsException;
import com.nttbootcamp.bootcoin.domain.beans.BootCoinExchangeDTO;
import com.nttbootcamp.bootcoin.domain.model.BootCoinExchange;
import com.nttbootcamp.bootcoin.domain.model.Transaction;
import com.nttbootcamp.bootcoin.domain.repository.BootCoinExchangeRepository;
import com.nttbootcamp.bootcoin.infraestructure.interfaces.IBootCoinExchangeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class BootCoinExchangeService implements IBootCoinExchangeService {

    @Autowired
    private BootCoinExchangeRepository repository;
    @Override
    public Flux<BootCoinExchange> findAll() {
        return repository.findAll();
    }

    @Override
    public Mono<BootCoinExchange> delete(String id) {
        return repository.findById(id)
                .flatMap(deleted -> repository.delete(deleted)
                        .then(Mono.just(deleted))).switchIfEmpty(Mono.error(new EntityNotExistsException()));

    }

    @Override
    public Mono<BootCoinExchange> findById(String id) {
        return repository.findById(id)
                .flatMap(bsp-> repository.findById(id))
                .switchIfEmpty(Mono.error(new EntityNotExistsException()));

    }

    @Override
    public Mono<BootCoinExchange> update(String id, Transaction request) {
        return null;
    }

    @Override
    public Mono<BootCoinExchange> save(BootCoinExchangeDTO dto) {
            BootCoinExchange a = BootCoinExchange.builder()
                .bootCoinExchange(dto.getBootCoinExchange())
                .build();
            return repository.save(a);

        }
}
