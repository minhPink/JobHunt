package com.minhdev.project.service;

import com.minhdev.project.domain.Company;
import com.minhdev.project.domain.Skill;
import com.minhdev.project.domain.response.ResultPaginationDTO;
import com.minhdev.project.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class SkillService {
    private final SkillRepository skillRepository;

    public SkillService(SkillRepository skillRepository) {
        this.skillRepository = skillRepository;
    }

    public boolean isNameExists(String name) {
        return this.skillRepository.existsByName(name);
    }

    public Skill create(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public Skill fetchSkillById(Long id) {
        return this.skillRepository.findById(id).orElse(null);
    }

    public Skill update(Skill skill) {
        return this.skillRepository.save(skill);
    }

    public ResultPaginationDTO getAllSkills(Specification<Skill> spec, Pageable pageable) {
        Page<Skill> page = skillRepository.findAll(spec, pageable);
        ResultPaginationDTO paginationDTO = new ResultPaginationDTO();
        ResultPaginationDTO.Meta meta = new ResultPaginationDTO.Meta();

        meta.setPage(pageable.getPageNumber() + 1);
        meta.setPageSize(pageable.getPageSize());
        meta.setPages(page.getTotalPages());
        meta.setTotal(page.getTotalElements());

        paginationDTO.setMeta(meta);
        paginationDTO.setResult(page.getContent());

        return paginationDTO;
    }

    public void deleteSkillById(Long id) {
        Optional<Skill> skill = this.skillRepository.findById(id);
        Skill currentSkill = skill.get();
        currentSkill.getJobs().forEach(job -> job.getSkills().remove(currentSkill));

        this.skillRepository.delete(currentSkill);
    }
}
