package com.nttbootcamp.bootcoin.domain.model;

import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Document
@ToString
public class BootCoinExchange {
    @Id
    private String id;
    @Digits(integer =20, fraction=6)
    private BigDecimal bootCoinExchange;
}
