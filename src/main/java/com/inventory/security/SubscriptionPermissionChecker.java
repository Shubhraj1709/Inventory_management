package com.inventory.security;

import com.inventory.entities.BusinessOwner;
import com.inventory.enums.SubscriptionLevel;

public class SubscriptionPermissionChecker {

    public static void checkAddEditPermission(BusinessOwner owner) {
    	if (owner.getSubscriptionPlan() != SubscriptionLevel.PAID &&
    		    owner.getSubscriptionPlan() != SubscriptionLevel.PREMIUM) {
    		    throw new RuntimeException("Upgrade your subscription to add or edit products.");
    		}
    }


    public static void checkAnalyticsPermission(BusinessOwner owner) {
        if (owner.getSubscriptionPlan() != SubscriptionLevel.PREMIUM) {
            throw new RuntimeException("Only premium users can access analytics.");
        }
    }
}
