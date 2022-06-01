package com.nttbootcamp.bootcoin.domain.enums;

public enum BootCoinTransactionType {
    BOOTCOIN_PAYMENT_ACCOUNT("BOOTCOIN PAYMENT WITH ACCOUNT"),
    BOOTCOIN_PAYMENT_YANKI("BootCoin PAYMENT WITH YANKI"),
    ACCOUNT_DEPOSIT("DEPOSIT IN ACCOUNT"),
    ACCOUNT_WITHDRAWAL("WITHDRAWAL IN ACCOUNT"),
    ACCOUNT_CREATION("ACCOUNT_CREATION"),
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL");
    public final String type;

    BootCoinTransactionType(String type) {
        this.type = type;
    }
}
