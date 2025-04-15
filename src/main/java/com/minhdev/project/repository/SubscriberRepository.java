package com.minhdev.project.repository;

import com.minhdev.project.domain.Subscriber;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

public interface SubscriberRepository extends JpaRepository<Subscriber, Integer>, JpaSpecificationExecutor<Subscriber> {
    boolean existsByEmail(String email);

    Subscriber findById(long id);
}
