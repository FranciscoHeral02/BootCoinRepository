package com.nttbootcamp.bootcoin.infraestructure.interfaces;

import com.nttbootcamp.bootcoin.domain.beans.AvailableAmountDTO;
import com.nttbootcamp.bootcoin.domain.beans.CreateBootCoinAccountDTO;
import com.nttbootcamp.bootcoin.domain.beans.CreateBootCoinAccountWithCardDTO;
import com.nttbootcamp.bootcoin.domain.model.BootCoinAccount;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.util.List;

public interface IBootCoinAccountService {
	Flux<BootCoinAccount> findAll();

	 Mono<BootCoinAccount> updateBalanceWithdrawal(String linkedAccount, BigDecimal balance);

	 Mono<BootCoinAccount> updateBalanceDeposit(String linkedAccount, BigDecimal balance);

	Mono<BootCoinAccount> createBootCoinAccount(CreateBootCoinAccountDTO account);
	Mono<BootCoinAccount> delete(String id);
	Mono<BootCoinAccount> findById(String id);
	Flux<BootCoinAccount> saveAll(List<BootCoinAccount> a);
	Mono<BootCoinAccount> update(BootCoinAccount request);
	//Mono<BootCoinAccount> findAllAccountsIn(Collection<String> accounts);
	Mono<BigDecimal> updateBalanceSend(String id, BigDecimal balance);
	Mono<BigDecimal> updateBalanceReceive(String id, BigDecimal balance);
	Mono<AvailableAmountDTO> getAvailableAmount(String accountNumber);
}
