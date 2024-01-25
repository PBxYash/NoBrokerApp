package com.nobroker.service;

import com.nobroker.payload.OwnerPlanDto;

public interface OwnerPlanService {
    public OwnerPlanDto subscribeOwnerPlan(long userId, int days);
    public void expirePlans();
}
