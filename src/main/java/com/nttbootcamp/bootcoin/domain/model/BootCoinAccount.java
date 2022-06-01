package com.nttbootcamp.bootcoin.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Digits;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
@ToString
public class BootCoinAccount {

	//BootCoin account field
	@Id
	private String cellphoneNumber;
	@Digits(integer =20, fraction=6)
	private BigDecimal balance;
	@NotNull
	private LocalDateTime createdDateTime;
	@NotNull
	private LocalDate createdDate;
	@NotNull	
	private Boolean valid;
	private String linkedAccount;
	private String email;
	private String docIdemType;
	private String docNum;
}