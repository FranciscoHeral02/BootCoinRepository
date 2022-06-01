package com.nttbootcamp.bootcoin.infraestructure.kafkaservices;

import com.nttbootcamp.bootcoin.domain.beans.BootCoinMessageDTO;
import com.nttbootcamp.bootcoin.domain.model.Account;
import com.nttbootcamp.bootcoin.domain.repository.BootCoinAccountRepository;
import com.nttbootcamp.bootcoin.domain.repository.AccountRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.function.Consumer;

@Component
public class KafkaInputStream {
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private BootCoinAccountRepository repository;

    @Bean
    Consumer<BootCoinMessageDTO> input(){
        return message->{
            switch (message.getTransactionType()) {
                case DEPOSIT:
                    System.out.println(message.getAccountNumber());
                    repository.findByLinkedAccount(message.getAccountNumber())
                            .flatMap(m->{   System.out.println(m);
                                            BigDecimal bigDecimal=m.getBalance().add(message.getAmount());
                                            m.setBalance(bigDecimal);

                                           return repository.save(m);
                            }).subscribe();

                    break;
                case WITHDRAWAL:
                    System.out.println(message.getAccountNumber());
                    repository.findById(message.getAccountNumber())
                            .flatMap(m->{   System.out.println(m);
                                BigDecimal bigDecimal=m.getBalance().subtract(message.getAmount());
                                m.setBalance(bigDecimal);

                                return repository.save(m);
                            }).subscribe();
                    break;
                case ACCOUNT_CREATION:
                    System.out.println(message);
                    Account d= Account.builder()
                                .amount(message.getAmount())
                                .accountNumber(message.getAccountNumber())
                                .businessPartnerId(message.getBusinessPartnerId())
                                .build();
                    accountRepository.save(d).subscribe();
                    break;
            }
        };
    }
}
