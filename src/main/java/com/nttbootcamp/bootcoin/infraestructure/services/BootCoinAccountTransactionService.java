package com.nttbootcamp.bootcoin.infraestructure.services;


import com.nttbootcamp.bootcoin.application.exception.EntityNotExistsException;
import com.nttbootcamp.bootcoin.application.exception.ResourceNotCreatedException;
import com.nttbootcamp.bootcoin.domain.beans.BootCoinMessageDTO;
import com.nttbootcamp.bootcoin.domain.beans.BootCoinOperationDTO;
import com.nttbootcamp.bootcoin.domain.enums.BootCoinTransactionType;
import com.nttbootcamp.bootcoin.domain.model.BootCoinAccount;
import com.nttbootcamp.bootcoin.domain.model.Transaction;
import com.nttbootcamp.bootcoin.domain.repository.TransactionRepository;
import com.nttbootcamp.bootcoin.infraestructure.interfaces.IBootCoinAccountTransactionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MimeTypeUtils;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.BiFunction;

@Service
public class BootCoinAccountTransactionService implements IBootCoinAccountTransactionService {

    //Services and Repositories
    @Autowired
    private TransactionRepository tRepository;
    @Autowired
    private BootCoinAccountService accountService;
    @Autowired
    private StreamBridge streamBridge;
    //Crud
    @Override
    public Flux<Transaction> findAll() {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Mono<Transaction> delete(String id) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Mono<Transaction> findById(String id) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Mono<ResponseEntity<Transaction>> update(String id, Transaction request) {
        // TODO Auto-generated method stub
        return null;
    }
    @Override
    public Flux<Transaction> saveAll(List<Transaction> a) {
        // TODO Auto-generated method stub
        return null;
    }

    //Business Logic

    @Override
    public Mono<Transaction> doBootCoinPayment(BootCoinOperationDTO dto) {
        Mono<BootCoinAccount> fromAccount = accountService.findById(dto.getFromCellphoneAccount())
                .switchIfEmpty(Mono.error(new EntityNotExistsException("Origin account doesn't exists")));
        Mono<BootCoinAccount> toAccount = accountService.findById(dto.getToCellphoneAccount())
                .switchIfEmpty(Mono.error(new EntityNotExistsException("Destiny account doesn't exists")));

        return  Mono.zip(fromAccount,toAccount)
                .filter(a-> !(dto.getFromCellphoneAccount().equals(dto.getToCellphoneAccount())))
                .switchIfEmpty(Mono.error(new ResourceNotCreatedException("Account cannot be the same")))
                .filter(a->a.getT1().getBalance().compareTo(dto.getAmount())>=0)
                .switchIfEmpty(Mono.error(new ResourceNotCreatedException("BootCoin account doesn't have sufficient funds")))
                .flatMap(a->saveTransactionPayment.apply(a,dto))
                .switchIfEmpty(Mono.error(new ResourceNotCreatedException("Transaction error")));

    }

    //Functions
    private final BiFunction<Tuple2<BootCoinAccount,BootCoinAccount>,BootCoinOperationDTO, Mono<Transaction>> saveTransactionPayment
            = (tuple2,dto) -> {

           Transaction t = Transaction.builder()
                .debit(dto.getAmount())
                .credit(dto.getAmount())
                .fromCellphoneAccount(tuple2.getT1().getCellphoneNumber())
                .toCellphoneAccount(tuple2.getT2().getCellphoneNumber())
                .transactiontype(BootCoinTransactionType.BOOTCOIN_PAYMENT_ACCOUNT)
                .createDate(LocalDate.now())
                .createDateTime(LocalDateTime.now())
                .build();

           return Mono.just(tuple2)
                    .flatMap(t1->{
                                        BootCoinMessageDTO w= BootCoinMessageDTO
                                                .builder()
                                                .businessPartnerId(t1.getT1().getDocNum())
                                                .accountNumber(t1.getT1().getLinkedAccount())
                                                .amount(dto.getAmount())
                                                .transactionType(BootCoinTransactionType.ACCOUNT_WITHDRAWAL)
                                                .build();
                                        streamBridge.send("output-out-0",w, MimeTypeUtils.APPLICATION_JSON);

                                    return accountService.updateBalanceSend(dto.getFromCellphoneAccount(),
                                            dto.getAmount()).thenReturn(t1);
                    })
                   .flatMap(t2-> {      BootCoinMessageDTO w = BootCoinMessageDTO
                                               .builder()
                                               .businessPartnerId(t2.getT2().getDocNum())
                                               .accountNumber(t2.getT2().getLinkedAccount())
                                               .amount(dto.getAmount())
                                               .transactionType(BootCoinTransactionType.ACCOUNT_DEPOSIT)
                                               .build();
                                       streamBridge.send("output-out-0", w, MimeTypeUtils.APPLICATION_JSON);

                                   return accountService.updateBalanceReceive(dto.getToCellphoneAccount(),
                                           dto.getAmount()).thenReturn(t2);

                   }).then(tRepository.save(t));
    };



}






