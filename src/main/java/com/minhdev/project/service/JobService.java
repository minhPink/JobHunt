package com.minhdev.project.service;

import com.minhdev.project.domain.Job;
import com.minhdev.project.domain.Skill;
import com.minhdev.project.domain.response.ResCreateJobDTO;
import com.minhdev.project.domain.response.ResUpdateJobDTO;
import com.minhdev.project.domain.response.ResultPaginationDTO;
import com.minhdev.project.repository.JobRepository;
import com.minhdev.project.repository.SkillRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class JobService {
    private final JobRepository jobRepository;
    private final SkillRepository skillRepository;

    public JobService(JobRepository jobRepository, SkillRepository skillRepository) {
        this.jobRepository = jobRepository;
        this.skillRepository = skillRepository;
    }

    public ResCreateJobDTO create(Job job) {
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(skill -> skill.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }
        Job createdJob = this.jobRepository.save(job);

        ResCreateJobDTO dto = new ResCreateJobDTO();
        dto.setId(createdJob.getId());
        dto.setName(createdJob.getName());
        dto.setSalary(createdJob.getSalary());
        dto.setQuantity(createdJob.getQuantity());
        dto.setLevel(createdJob.getLevel());
        dto.setLocation(createdJob.getLocation());
        dto.setStartDate(createdJob.getStartDate());
        dto.setEndDate(createdJob.getEndDate());
        dto.setActive(createdJob.isActive());
        dto.setCreatedAt(createdJob.getCreatedAt());
        dto.setCreatedBy(createdJob.getCreatedBy());

        if (createdJob.getSkills() != null) {
            List<String> skills = createdJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return dto;
    }

    public Job fetchJbById(Long id) {
        return this.jobRepository.findById(id).orElse(null);
    }

    public ResUpdateJobDTO update(Job job) {
        if (job.getSkills() != null) {
            List<Long> reqSkills = job.getSkills()
                    .stream().map(skill -> skill.getId())
                    .collect(Collectors.toList());
            List<Skill> dbSkills = this.skillRepository.findByIdIn(reqSkills);
            job.setSkills(dbSkills);
        }
        Job updateJob = this.jobRepository.save(job);

        ResUpdateJobDTO dto = new ResUpdateJobDTO();
        dto.setId(updateJob.getId());
        dto.setName(updateJob.getName());
        dto.setSalary(updateJob.getSalary());
        dto.setQuantity(updateJob.getQuantity());
        dto.setLevel(updateJob.getLevel());
        dto.setLocation(updateJob.getLocation());
        dto.setStartDate(updateJob.getStartDate());
        dto.setEndDate(updateJob.getEndDate());
        dto.setActive(updateJob.isActive());
        dto.setUpdateBy(updateJob.getUpdatedBy());
        dto.setUpdatedAt(updateJob.getUpdatedAt());

        if (updateJob.getSkills() != null) {
            List<String> skills = updateJob.getSkills()
                    .stream().map(item -> item.getName())
                    .collect(Collectors.toList());
            dto.setSkills(skills);
        }
        return dto;
    }

    public void delete(long id) {
        this.jobRepository.deleteById(id);
    }

    public ResultPaginationDTO getAllJobs(Specification<Job> spec, Pageable pageable) {
        Page<Job> page = jobRepository.findAll(spec, pageable);
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
}
