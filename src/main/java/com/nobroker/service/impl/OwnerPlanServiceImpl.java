package com.nobroker.service.impl;
import com.nobroker.Repository.OwnerPlanRepository;
import com.nobroker.Repository.UserRepository;
import com.nobroker.entity.OwnerPlan;
import com.nobroker.entity.User;
import com.nobroker.payload.OwnerPlanDto;
import com.nobroker.service.OwnerPlanService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.security.auth.Subject;
import java.time.Duration;
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

    @Autowired
     private  JavaMailSender javaMailSender;

    @Override
    public OwnerPlanDto subscribeOwnerPlan(long userId, int days) {
        int hours = 24;
        int minutes = 59;

        User user = userRepository.findById(userId).orElse(null);

        OwnerPlan save1 = null;
        if (user != null) {
            OwnerPlan ownerPlan1 = new OwnerPlan();
            ownerPlan1.setUserId(userId);
            ownerPlan1.setSubscriptionActive(true);
            ownerPlan1.setSubscriptionActiveDate(LocalDateTime.now());
            ownerPlan1.setSubscriptionExpirationDate(LocalDateTime.now().plusDays(days));
            ownerPlan1.setDays(days);
            ownerPlan1.setHours(hours);
            ownerPlan1.setMinutes(minutes);
            save1 = ownerPlanRepository.save(ownerPlan1);
        }

        OwnerPlanDto ownerPlanDto = maptoDto(save1);

        return ownerPlanDto;
    }
//    public void updateSubscription(OwnerPlan subscription) {
//        ownerPlanRepository.save(subscription);
//    }

    @Scheduled(fixedDelay = 60000) // Run every minute
    public void updateSubscriptionTimes() {
        List<OwnerPlan> activePlans = ownerPlanRepository.findBySubscriptionActive(true);
        for (OwnerPlan subscription : activePlans) {
            updateSubscriptionTime(subscription);
        }
    }


    private void updateSubscriptionTime(OwnerPlan subscription) {
        // Update minutes, hours, days based on current time and start date
        LocalDateTime now = LocalDateTime.now();
        Duration duration = Duration.between(subscription.getSubscriptionActiveDate(), now);

        long minutes = duration.toMinutes();
        long hours = duration.toHours() % 24;
        long days = duration.toDays();

        // Calculate the remaining time in minutes after subtracting the initial set minutes
        long remainingMinutes = subscription.getMinutes() - minutes;
        int min = 0;
        if(subscription.getHours()==0&&subscription.getDays()==0){
            min=0;
        }else {
            min = 59;
        }

        if (remainingMinutes < 0) {
            System.out.println("added");
            // Reset minutes to the initial set value and decrement hours or days accordingly
            subscription.setMinutes(min);

            if (subscription.getHours() > 0) {
                subscription.setHours(subscription.getHours() - 1);
            } else if (subscription.getDays() > 0) {
                // Add hours back if there are remaining days
                subscription.setHours(24);
                subscription.setDays(subscription.getDays() - 1);
            }

            if (subscription.getDays() < 0) {
                // If days go below zero, expire the subscription
                subscription.setSubscriptionActive(true);
            }
        } else {
            subscription.setMinutes((int) remainingMinutes);
        }

        // Save updated subscription
        ownerPlanRepository.save(subscription);

        // ... (the rest of your code)
        if(subscription.getDays()==3&& subscription.getHours() == 0 && subscription.getMinutes() == 2){
            String Subject = "Subscription Expiration Notification";
            String Text = "Dear user, your subscription will expire in 3 days. Please renew your subscription to continue using our services.";
            User user = userRepository.findById(subscription.getUserId()).get();
            sendExpirationEmail(user.getEmail(),Subject,Text);
        }
        if ((subscription.getDays() == 0 && subscription.getHours() == 0 && subscription.getMinutes() == 0)) {
            subscription.setHours(0);
            subscription.setMinutes(0);
            // Subscription expired, handle accordingly
            subscription.setSubscriptionActive(false);
            User user = userRepository.findById(subscription.getUserId()).get();
            String Subject = "Your subscription has expired";
            String Text = "Dear user, your subscription has expired. Please renew your subscription to continue using our services.";
            sendExpirationEmail(user.getEmail(),Subject,Text);
            ownerPlanRepository.save(subscription);
    }}

    private void sendExpirationEmail(String userEmail, String Subject, String Text) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(userEmail);
        message.setSubject(Subject);
        message.setText(Text);
        javaMailSender.send(message);
    }

    private OwnerPlanDto maptoDto(OwnerPlan ownerPlan) {
        return modelMapper.map(ownerPlan ,OwnerPlanDto.class);
    }
}
