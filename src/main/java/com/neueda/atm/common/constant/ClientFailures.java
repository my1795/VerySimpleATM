package com.neueda.atm.common.constant;

public enum ClientFailures {

    POLICY_ERROR("PE1000", "Request policy is violated"),

    SECURITY_ERROR("SE1000", "Security policy is violated"),

    RESOURCE_ERROR("RE1000", "Resource is not found");

    private String reasonCode;
    private String reasonText;

    ClientFailures(final String reasonCode, final String reasonText) {
        this.reasonCode = reasonCode;
        this.reasonText = reasonText;
    }
    public String getReasonCode() {
        return reasonCode;
    }

    public String getReasonText() {
        return reasonText;
    }

    public static ClientFailures getFailureByReasonCode(String reasonCode) {
        for (ClientFailures failureListReasons : ClientFailures.values()) {
            if (failureListReasons.getReasonCode().equalsIgnoreCase(reasonCode)) {
                return failureListReasons;
            }
        }
        throw new IllegalArgumentException("Invalid reasonCode: " + reasonCode);
    }
}
