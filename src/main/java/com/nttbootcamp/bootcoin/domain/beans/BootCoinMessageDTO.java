package com.nttbootcamp.bootcoin.domain.beans;

import com.nttbootcamp.bootcoin.domain.enums.BootCoinTransactionType;
import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BootCoinMessageDTO {
    private String accountNumber;
    private String businessPartnerId;
    private BigDecimal amount;
    private BootCoinTransactionType transactionType;
}
