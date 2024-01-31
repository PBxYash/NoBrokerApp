package com.nobroker.service;

import com.nobroker.entity.OwnerPlan;
import com.nobroker.payload.OwnerPlanDto;

public interface OwnerPlanService {
    static void updateSubscription(OwnerPlan subscription) {
    }

    public OwnerPlanDto subscribeOwnerPlan(long userId, int days);
    
}
