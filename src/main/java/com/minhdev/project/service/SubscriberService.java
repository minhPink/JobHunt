package com.minhdev.project.service;

import com.minhdev.project.domain.Skill;
import com.minhdev.project.domain.Subscriber;
import com.minhdev.project.repository.SkillRepository;
import com.minhdev.project.repository.SubscriberRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class SubscriberService {
    private final SubscriberRepository subscriberRepository;
    private final SkillRepository skillRepository;

    public SubscriberService(SubscriberRepository subscriberRepository, SkillRepository skillRepository) {
        this.subscriberRepository = subscriberRepository;
        this.skillRepository = skillRepository;
    }

    public boolean existsByEmail(String email) {
        return this.subscriberRepository.existsByEmail(email);
    }

    public Subscriber create(Subscriber subscriber) {
        if (subscriber.getSkills() != null) {
            List<Long> reqSkill = subscriber.getSkills()
                    .stream().map(s -> s.getId()).collect(Collectors.toList());

            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkill);
            subscriber.setSkills(dbSkills);
        }

        return this.subscriberRepository.save(subscriber);
    }

    public Subscriber findById(long id) {
        return this.subscriberRepository.findById(id);
    }

    public Subscriber update(Subscriber subscriber, Subscriber subscriberReq) {
        if (subscriber.getSkills() != null) {
            List<Long> reqSkills = subscriberReq.getSkills()
                    .stream().map(s -> s.getId()).collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            subscriber.setSkills(dbSkills);
        }
        return this.subscriberRepository.save(subscriber);
    }

}
