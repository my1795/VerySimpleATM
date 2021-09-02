package com.neueda.atm.common.constant;

public enum RequestType {

    BALANCE_CHECK("checkBalance"),

    WITHDRAW("withdraw"),

    DEPOSIT("deposit");


    private final String name;

    RequestType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static RequestType getByName(String aName) {
        for (RequestType current : RequestType.values()) {
            if (current.getName().equalsIgnoreCase(aName.trim())) {
                return current;
            }
        }
        throw new IllegalArgumentException("Invalid RequestType: " + aName);
    }
}
