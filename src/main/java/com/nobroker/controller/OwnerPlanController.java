package com.nobroker.controller;


import com.nobroker.payload.OwnerPlanDto;
import com.nobroker.service.OwnerPlanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@RestController
@RequestMapping("/api/owner-plans")
public class OwnerPlanController {

    @Autowired
    private OwnerPlanService ownerPlanService;

    @PostMapping("/subscribe/{userId}/{days}")
    public ResponseEntity<OwnerPlanDto> subscribeOwnerPlan(@PathVariable long userId, @PathVariable int days) {
        OwnerPlanDto ownerPlanDto = ownerPlanService.subscribeOwnerPlan(userId, days);
        return  new ResponseEntity<>(ownerPlanDto, HttpStatus.CREATED);
    }
}

