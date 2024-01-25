package com.nobroker.service.impl;

import com.nobroker.Repository.OwnerPlanRepository;
import com.nobroker.Repository.UserRepository;
import com.nobroker.entity.OwnerPlan;
import com.nobroker.entity.SubscriptionDuration;
import com.nobroker.entity.User;
import com.nobroker.payload.OwnerPlanDto;
import com.nobroker.service.OwnerPlanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class OwnerPlanServiceImpl implements OwnerPlanService {

    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private OwnerPlanRepository ownerPlanRepository;

    @Autowired
    private UserRepository userRepository;

    @Override
    public OwnerPlanDto subscribeOwnerPlan(long userId, int days) {
        int hours = 24;
        int minutes = 60;

        User user = userRepository.findById(userId).orElse(null);

        OwnerPlan save1 = null;
        if (user != null) {
            OwnerPlan ownerPlan1 = new OwnerPlan();
            ownerPlan1.setUserId(userId);
            ownerPlan1.setSubscriptionActive(true);
            ownerPlan1.setSubscriptionActiveDate(LocalDateTime.now());
            ownerPlan1.setSubscriptionExpirationDate(LocalDateTime.now());
            ownerPlan1.setSubscriptionDuration(new SubscriptionDuration(days, hours, minutes));
            save1 = ownerPlanRepository.save(ownerPlan1);
        }
        OwnerPlanDto ownerPlanDto = maptoDto(save1);

        return ownerPlanDto;
    }


    @Scheduled(fixedRate = 1 * 60 * 1000) // Run every 1 minute
    public void expirePlans() {
        List<OwnerPlan> activePlans = ownerPlanRepository.findBySubscriptionActive(true);

        for (OwnerPlan plan : activePlans) {
            LocalDateTime expirationDateTime = plan.getSubscriptionExpirationDate();
            LocalDateTime currentDateTime = LocalDateTime.now();

            // Calculate the elapsed time in minutes
            long minutesElapsed = Duration.between(expirationDateTime, currentDateTime).toMinutes();

            // Update the expiration date and time based on elapsed minutes
            plan.setSubscriptionExpirationDate(expirationDateTime.minusMinutes(minutesElapsed));

            // Update subscription duration
            plan.reduceRemainingTime();

            // Reduce minutes by 1 every minute
            plan.getSubscriptionDuration().reduceByOneMinute();

            // Check if 60 minutes have passed, then reduce hours
            if (minutesElapsed >= 60) {
                plan.getSubscriptionDuration().reduceByOneHour();

                // Check if 24 hours have passed, then reduce days
                if (minutesElapsed >= 24 * 60) {
                    plan.getSubscriptionDuration().reduceByOneDay();
                }
            }

            // Save the updated plan in the repository
            ownerPlanRepository.save(plan);
        }
    }

    OwnerPlanDto maptoDto(OwnerPlan ownerPlan) {
        return modelMapper.map(ownerPlan, OwnerPlanDto.class);
    }
}
