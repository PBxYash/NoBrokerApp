package com.nobroker.payload;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;
@Data
@AllArgsConstructor
@NoArgsConstructor
public class OwnerPlanDto {

    private long ownerPlanId;


    private long userId;

    private boolean subscriptionActive;


    private LocalDateTime subscriptionActiveDate;

    private LocalDateTime subscriptionExpirationDate;

    private int days;

    private int hours;

    private int minutes;



}