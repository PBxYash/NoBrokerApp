package com.nobroker.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;


@Entity
@Table(name = "owner_plan")
@Data
public class OwnerPlan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long ownerPlanId;

    @Column(name = "user_id", unique = true)
    private long userId;

    private boolean subscriptionActive;


    @Column(name = "subscription_active_date")
    private LocalDateTime subscriptionActiveDate;

    @Column(name = "subscription_expiration_date")
    private LocalDateTime subscriptionExpirationDate;

    private int days;

    private int hours;

    private int minutes;

}
