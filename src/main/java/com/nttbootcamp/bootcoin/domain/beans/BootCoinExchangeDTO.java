package com.nttbootcamp.bootcoin.domain.beans;

import lombok.*;

import javax.validation.constraints.Digits;
import java.math.BigDecimal;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BootCoinExchangeDTO {
    @Digits(integer =20, fraction=6)
    private BigDecimal bootCoinExchange;
}
