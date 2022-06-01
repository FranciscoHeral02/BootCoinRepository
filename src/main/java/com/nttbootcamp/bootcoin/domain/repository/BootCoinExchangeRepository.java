package com.nttbootcamp.bootcoin.domain.repository;


import com.nttbootcamp.bootcoin.domain.model.BootCoinExchange;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface BootCoinExchangeRepository extends ReactiveMongoRepository<BootCoinExchange,String> {
}
