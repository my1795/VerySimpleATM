package com.neueda.atm.resource;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.io.Serializable;
import java.util.Objects;

@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountRequestWrapper implements Serializable {
    private int pin;
    private int requestAmount;


    public int getPin() {
        return pin;
    }

    public void setPin(int pin) {
        this.pin = pin;
    }

    public int getRequestAmount() {
        return requestAmount;
    }

    public void setRequestAmount(int requestAmount) {
        this.requestAmount = requestAmount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AccountRequestWrapper)) return false;
        AccountRequestWrapper that = (AccountRequestWrapper) o;
        return getPin() == that.getPin() && getRequestAmount() == that.getRequestAmount();
    }

    @Override
    public int hashCode() {
        return Objects.hash(getPin(), getRequestAmount());
    }

}
