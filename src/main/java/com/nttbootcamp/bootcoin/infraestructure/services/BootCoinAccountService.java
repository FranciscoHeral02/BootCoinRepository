package com.nttbootcamp.bootcoin.infraestructure.services;


import com.nttbootcamp.bootcoin.application.exception.EntityAlreadyExistsException;
import com.nttbootcamp.bootcoin.application.exception.EntityNotExistsException;
import com.nttbootcamp.bootcoin.application.exception.ResourceNotCreatedException;
import com.nttbootcamp.bootcoin.domain.beans.AvailableAmountDTO;
import com.nttbootcamp.bootcoin.domain.beans.CreateBootCoinAccountDTO;
import com.nttbootcamp.bootcoin.domain.model.Account;
import com.nttbootcamp.bootcoin.domain.model.BootCoinAccount;
import com.nttbootcamp.bootcoin.domain.model.BootCoinExchange;
import com.nttbootcamp.bootcoin.domain.repository.BootCoinAccountRepository;
import com.nttbootcamp.bootcoin.domain.repository.AccountRepository;
import com.nttbootcamp.bootcoin.infraestructure.interfaces.IBootCoinAccountService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.stream.function.StreamBridge;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.function.Function;

@Service
public class BootCoinAccountService implements IBootCoinAccountService {

    //Repositories and Services
    @Autowired
    private BootCoinAccountRepository repository;
    @Autowired
    private StreamBridge streamBridge;
    @Autowired
    private BootCoinExchangeService exchange;
    @Autowired
    private AccountRepository debitCardRepository;

    // Crud
    @Override
    public Flux<BootCoinAccount> findAll() {
        return repository.findAll();
    }
    @Override
    public Mono<BootCoinAccount> delete(String Id) {
        return repository.findById(Id).flatMap(deleted -> repository.delete(deleted).then(Mono.just(deleted)))
        				 .switchIfEmpty(Mono.error(new EntityNotExistsException()));
    }
    @Override
    public Mono<BootCoinAccount> findById(String id) {
        return repository.findById(id)
                .switchIfEmpty(Mono.error(new EntityNotExistsException("Account doesn't exists")));
    }
    @Override
    public Flux<BootCoinAccount> saveAll(List<BootCoinAccount> a) {

        return repository.saveAll(a);
    }
    @Override
    public Mono<BootCoinAccount> update(BootCoinAccount _request) {

        return repository.findById(_request.getCellphoneNumber()).flatMap(a -> {
            a.setCellphoneNumber(_request.getCellphoneNumber());
            a.setValid(_request.getValid());
            a.setDocIdemType(_request.getDocIdemType());
            a.setDocNum(_request.getDocNum());
            a.setEmail(_request.getEmail());
            a.setValid(_request.getValid());
            a.setLinkedAccount(_request.getLinkedAccount());
            return repository.save(a);
        }).switchIfEmpty(Mono.error(new EntityNotExistsException()));
    }
    @Override
    public Mono<AvailableAmountDTO> getAvailableAmount(String cellphoneNumber) {
      return repository.findById(cellphoneNumber)
                .switchIfEmpty(Mono.error(new EntityNotExistsException("Account doesn't exists")))
                .map(a-> AvailableAmountDTO.builder()
                            .cellphoneNumber(a.getCellphoneNumber())
                            .availableAmount(a.getBalance()).build());
    }

    @Override
    public Mono<BigDecimal> updateBalanceSend(String id, BigDecimal balance) {
        Mono<BootCoinExchange> ex= exchange.findAll().last();

        return repository.findById(id)
                .zipWith(ex)
                .filter(a->(balance.divide(a.getT2().getBootCoinExchange())).compareTo(a.getT1().getBalance())<=0)
                .switchIfEmpty(Mono.error(new ResourceNotCreatedException("Withdrawal is more than actual balance")))
                .flatMap(a ->
                {   BigDecimal bigDecimal=a.getT1().getBalance().subtract(balance.divide(a.getT2().getBootCoinExchange()));
                    a.getT1().setBalance(bigDecimal);
                    return repository.save(a.getT1()).map(b->b.getBalance());
                });
    }
    @Override
    public Mono<BigDecimal> updateBalanceReceive(String id, BigDecimal balance) {
        Mono<BootCoinExchange> ex= exchange.findAll().last();

        return repository.findById(id)
                .zipWith(ex)
                .flatMap(a ->
                {    BigDecimal bigDecimal=a.getT1().getBalance().subtract(balance.divide(a.getT2().getBootCoinExchange()));
                     a.getT1().setBalance(bigDecimal);
                     return repository.save(a.getT1()).map(b->b.getBalance());
                });

    }
    @Override
    public Mono<BootCoinAccount> updateBalanceWithdrawal(String linkedAccount, BigDecimal balance) {
                 return repository.findByLinkedAccount(linkedAccount)
                        .filter(a->balance.compareTo(a.getBalance())<=0)
                        .switchIfEmpty(Mono.error(new ResourceNotCreatedException("Withdrawal is more than actual balance")))
                        .flatMap(a -> {   BigDecimal bigDecimal=a.getBalance().subtract(balance);
                                          a.setBalance(bigDecimal);
                                           return repository.save(a);
                        });
    }
    @Override
    public Mono<BootCoinAccount> updateBalanceDeposit(String linkedAccount, BigDecimal balance) {
                return repository.findByLinkedAccount(linkedAccount)
                         .flatMap(m->{ System.out.println(m);
                                        BigDecimal bigDecimal=m.getBalance().add(balance);
                                        m.setBalance(bigDecimal);
                                        return repository.save(m);
                         });

    }



    @Override
    public Mono<BootCoinAccount> createBootCoinAccount(CreateBootCoinAccountDTO account) {

        Mono<Boolean> existPhone = repository.existsByCellphoneNumber(account.getCellphoneNumber());
        Mono<Account> debitCard = debitCardRepository.findByAccountNumberAndBusinessPartnerId(
                account.getAccountNumber(), account.getDocNum())
                .switchIfEmpty(Mono.error(new EntityAlreadyExistsException("Account doesn't exists")));

        return  existPhone
                .filter(exist->!exist)
                .switchIfEmpty(Mono.error(new EntityAlreadyExistsException("Account already exists")))
                .then(Mono.just(account)).zipWith(debitCard)
                .flatMap(t -> mapToAccountAndSave1.apply(t));
    }



    private final Function<Tuple2<CreateBootCoinAccountDTO, Account>, Mono<BootCoinAccount>> mapToAccountAndSave1 = dto -> {

        BootCoinAccount a = BootCoinAccount.builder()
                .valid(true)
                .balance(dto.getT2().getAmount())
                .cellphoneNumber(dto.getT1().getCellphoneNumber())
                .docIdemType(dto.getT1().getDocIdemType())
                .docNum(dto.getT1().getDocNum())
                .createdDate(LocalDate.now())
                .createdDateTime(LocalDateTime.now())
                .email(dto.getT1().getEmail())
                .linkedAccount(dto.getT2().getAccountNumber())
                .build();
        return repository.save(a);
    };

}
