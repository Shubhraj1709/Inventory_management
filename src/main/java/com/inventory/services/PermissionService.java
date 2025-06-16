package com.inventory.services;

import com.inventory.entities.User;
import com.inventory.enums.SubscriptionLevel;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.Map;
import java.util.Set;

@Service
public class PermissionService {

	public Map<String, Set<String>> getPermissionsForLevel(SubscriptionLevel level) {
	    return PLAN_PERMISSIONS.getOrDefault(level, Map.of());
	}

    private static final Map<SubscriptionLevel, Map<String, Set<String>>> PLAN_PERMISSIONS = Map.of(
        SubscriptionLevel.FREE, Map.of(
            "invoices", Set.of("view")
        ),
        SubscriptionLevel.PAID, Map.of(
            "invoices", Set.of("view", "generate", "send", "mark-paid"),
            "products", Set.of("view", "add", "update", "delete")
        ),
        SubscriptionLevel.PREMIUM, Map.of(
            "invoices", Set.of("view", "generate", "send", "mark-paid"),
            "products", Set.of("view", "add", "update", "delete"),
            "employees", Set.of("view", "add", "update", "delete")
        )
    );

    public boolean hasPermission(User user, String module, String action) {
        if (user.getPlanStart() == null || user.getPlanEnd() == null) return false;

        LocalDate now = LocalDate.now();
        if (now.isBefore(user.getPlanStart()) || now.isAfter(user.getPlanEnd())) return false;

        SubscriptionLevel level = SubscriptionLevel.fromDbValue(user.getSubscriptionPlan());
        Map<String, Set<String>> permissions = PLAN_PERMISSIONS.get(level);
        if (permissions == null) return false;

        Set<String> actions = permissions.get(module);
        return actions != null && actions.contains(action);
    }
}
