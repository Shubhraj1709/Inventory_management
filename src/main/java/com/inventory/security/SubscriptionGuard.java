package com.inventory.security;

import com.inventory.entities.Subscription;
import com.inventory.enums.PlanType;

public class SubscriptionGuard {

    public static boolean hasAccess(String requiredLevel, Subscription subscription) {
        if (subscription == null || subscription.getPlan() == null) {
            System.out.println("❌ Subscription or Plan is null");
            return false;
        }

        PlanType currentPlanType = subscription.getPlan().getPlanType();
        System.out.println("🔍 Checking access: Required=" + requiredLevel + ", ActualPlanType=" + currentPlanType);

        int actualWeight = getPlanWeight(currentPlanType);
        int requiredWeight = getLevelWeight(requiredLevel);

        return actualWeight >= requiredWeight;
    }

    private static int getPlanWeight(PlanType type) {
        return switch (type) {
            case BASIC -> 0;
            case PREMIUM -> 2;
            case ENTERPRISE -> 3;
        };
    }

    private static int getLevelWeight(String level) {
        return switch (level.toUpperCase()) {
            case "FREE" -> 0;
            case "PAID" -> 1;
            case "PREMIUM" -> 2;
            default -> Integer.MAX_VALUE; // Unknown levels = deny access
        };
    }
    
}



//package com.inventory.security;
//
//import com.inventory.entities.Subscription;
//import com.inventory.enums.PlanType;
//import com.inventory.enums.SubscriptionLevel;
//
//public class SubscriptionGuard {
//	
//	 public static boolean hasAccess(String requiredLevel, Subscription subscription) {
//	        if (subscription == null || subscription.getPlan() == null) {
//	            System.out.println("❌ Subscription or Plan is null");
//	            return false;
//	        }
//
//	        // Convert PlanType to SubscriptionLevel
//	        SubscriptionLevel actualLevel = switch (subscription.getPlan().getPlanType()) {
//	            case BASIC -> SubscriptionLevel.FREE;
//	            case PREMIUM -> SubscriptionLevel.PAID;
//	            case ENTERPRISE -> SubscriptionLevel.PREMIUM;
//	        };
//
//	        System.out.println("🔍 Checking access: Required=" + requiredLevel + ", Actual=" + actualLevel);
//
//	        try {
//	            SubscriptionLevel required = SubscriptionLevel.valueOf(requiredLevel.toUpperCase());
//	            return actualLevel.ordinal() >= required.ordinal();
//	        } catch (IllegalArgumentException e) {
//	            System.out.println("❌ Invalid required level: " + requiredLevel);
//	            return false;
//	        }
//	        
//	        
//	        
//	 }
//}






//	public static boolean hasAccess(String requiredLevel, Subscription subscription) {
//	    if (subscription == null || subscription.getPlan() == null) {
//	        System.out.println("No subscription or plan found!");
//	        return false;
//	    }
//
//	    PlanType actual = subscription.getPlan().getPlanType();
//	    System.out.println("Required Level: " + requiredLevel);
//	    System.out.println("Actual PlanType: " + actual);
//
//	    return switch (requiredLevel) {
//	        case "BASIC" -> true;
//	        case "PREMIUM" -> actual == PlanType.PREMIUM || actual == PlanType.ENTERPRISE;
//	        case "ENTERPRISE" -> actual == PlanType.ENTERPRISE;
//	        default -> false;
//	    };
//	}

//}

