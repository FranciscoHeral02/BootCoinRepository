package com.nttbootcamp.bootcoin.domain.repository;



import com.nttbootcamp.bootcoin.domain.model.Transaction;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;


public interface TransactionRepository extends ReactiveMongoRepository<Transaction, String> {
   /* Mono<Long> countByAccountAndTransactiontypeIn(String accountNumber,Collection<String> tType);
    Mono<Long> countByAccountAndTransactiontypeInAndCreateDateBetween(String accountNumber,
                                                                      Collection<String> tType,
                                                                      LocalDate startDate,
                                                                      LocalDate endDate);
    Flux<Transaction> findByDebitCardIdOrderByCreateDateDesc(String debitCardNumber);

    Flux<FeeCharged> findByAccountAndCreateDateBetweenAndCommissionAmountGreaterThan(String accountNumber,
                                                                                     LocalDate startDate,
                                                                                     LocalDate endDat,
                                                                                     BigDecimal limit);
*/
}
