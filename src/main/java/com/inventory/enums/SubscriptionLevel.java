package com.inventory.enums;


public enum SubscriptionLevel {
    FREE, PAID, PREMIUM, BASIC;

    public String toDbValue() {
        return switch (this) {
            case FREE -> "BASIC";
            case PAID -> "PREMIUM";
            case PREMIUM -> "ENTERPRISE";
            case BASIC -> "BASIC";
        };
    }

    public static SubscriptionLevel fromDbValue(String dbValue) {
        return switch (dbValue.toUpperCase()) {
            case "BASIC" -> FREE;
            case "PREMIUM" -> PAID;
            case "ENTERPRISE" -> PREMIUM;
            default -> throw new IllegalArgumentException("Unknown dbValue: " + dbValue);
        };
    }
}



//public enum SubscriptionPlan {
//    BASIC,
//    PREMIUM,
//    ENTERPRISE
//}
