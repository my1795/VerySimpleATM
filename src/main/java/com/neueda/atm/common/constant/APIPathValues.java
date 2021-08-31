package com.neueda.atm.common.constant;

public class APIPathValues {
    private APIPathValues() {
        throw new IllegalStateException("Utility class");
    }
    public static final String REQUEST_ACCOUNTS_BASE_URL = "/atm/v1/accounts/{accountNumber}/";
}
