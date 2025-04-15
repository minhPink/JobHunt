package com.minhdev.project.controller;

import com.minhdev.project.domain.Subscriber;
import com.minhdev.project.service.SubscriberService;
import com.minhdev.project.util.annotation.ApiMessage;
import com.minhdev.project.util.error.CustomizeException;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SubscriberController {
    private final SubscriberService subscriberService;

    public SubscriberController(SubscriberService subscriberService) {
        this.subscriberService = subscriberService;
    }

    @PostMapping("/subscribers")
    @ApiMessage("Create a subscriber")
    public ResponseEntity<Subscriber> create(@Valid @RequestBody Subscriber subscriber) throws CustomizeException {
        boolean isExist = this.subscriberService.existsByEmail(subscriber.getEmail());
        if (isExist) {
            throw new CustomizeException("Subscriber already exists");
        }

        return ResponseEntity.status(HttpStatus.CREATED).body(this.subscriberService.create(subscriber));
    }

    @PutMapping("/subscribers")
    @ApiMessage("Update a subscriber")
    public ResponseEntity<Subscriber> update(@Valid @RequestBody Subscriber subscriber) throws CustomizeException {
        Subscriber susDB = this.subscriberService.findById(subscriber.getId());
        if (susDB == null) {
            throw new CustomizeException("Subscriber not found");
        }
        return ResponseEntity.ok().body(this.subscriberService.update(susDB, subscriber));
    }

}
