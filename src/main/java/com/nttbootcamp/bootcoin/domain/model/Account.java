package com.nttbootcamp.bootcoin.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.math.BigDecimal;
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document
public class Account {
    @Id
    private String accountNumber;
    private String businessPartnerId;
    private BigDecimal amount;
}
