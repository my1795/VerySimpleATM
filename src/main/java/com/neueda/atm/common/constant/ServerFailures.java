package com.neueda.atm.common.constant;

public enum ServerFailures {

    UNKNOWN_ERROR("SE1000", "Unexpected error is occurred. Please contact with administrator");




    private String reasonCode;
    private String reasonText;
    ServerFailures(final String reasonCode, final String reasonText) {
        this.reasonCode = reasonCode;
        this.reasonText = reasonText;
    }
    public String getReasonCode() {
        return reasonCode;
    }

    public String getReasonText() {
        return reasonText;
    }

    public static ServerFailures getFailureByReasonCode(String reasonCode) {
        for (ServerFailures failureListReasons : ServerFailures.values()) {
            if (failureListReasons.getReasonCode().equalsIgnoreCase(reasonCode)) {
                return failureListReasons;
            }
        }
        throw new IllegalArgumentException("Invalid reasonCode: " + reasonCode);
    }
}
