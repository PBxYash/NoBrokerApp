package com.nobroker.Repository;

import com.nobroker.entity.OwnerPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OwnerPlanRepository extends JpaRepository<OwnerPlan, Long> {
    List<OwnerPlan> findBySubscriptionActive(boolean b);
}
