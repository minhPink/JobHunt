package com.minhdev.project.service;

import com.minhdev.project.domain.Job;
import com.minhdev.project.domain.Resume;
import com.minhdev.project.domain.User;
import com.minhdev.project.domain.response.ResultPaginationDTO;
import com.minhdev.project.domain.response.resume.ResCreateResumeDTO;
import com.minhdev.project.domain.response.resume.ResFetchResumeDTO;
import com.minhdev.project.domain.response.resume.ResUpdateResumeDTO;
import com.minhdev.project.domain.response.user.ResUserDTO;
import com.minhdev.project.repository.JobRepository;
import com.minhdev.project.repository.ResumeRepository;
import com.minhdev.project.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ResumeService {
    private final ResumeRepository resumeRepository;
    private final UserRepository userRepository;
    private final JobRepository jobRepository;

    public ResumeService(ResumeRepository resumeRepository, UserRepository userRepository, JobRepository jobRepository) {
        this.resumeRepository = resumeRepository;
        this.userRepository = userRepository;
        this.jobRepository = jobRepository;
    }

    public boolean checkResumeExistsByUserAndJob(Resume resume) {
        if (resume.getUser() == null) {
            return false;
        }
        Optional<User> userOptional = userRepository.findById(resume.getUser().getId());
        if (userOptional.isEmpty()) {
            return false;
        }

        if (resume.getJob() == null) {
            return false;
        }
        Optional<Job> jobOptional = jobRepository.findById(resume.getJob().getId());
        if (jobOptional.isEmpty()) {
            return false;
        }

        return true;
    }

    public ResCreateResumeDTO create(Resume resume) {
        resume = this.resumeRepository.save(resume);

        ResCreateResumeDTO  resCreateResumeDTO = new ResCreateResumeDTO();
        resCreateResumeDTO.setId(resume.getId());
        resCreateResumeDTO.setCreatedAt(resume.getCreatedAt());
        resCreateResumeDTO.setCreatedBy(resume.getCreatedBy());

        return resCreateResumeDTO;
    }

    public ResUpdateResumeDTO update(Resume resume) {
        resume = this.resumeRepository.save(resume);
        ResUpdateResumeDTO  resUpdateResumeDTO = new ResUpdateResumeDTO();
        resUpdateResumeDTO.setUpdatedAt(resume.getUpdatedAt());
        resUpdateResumeDTO.setUpdatedBy(resume.getUpdatedBy());

        return resUpdateResumeDTO;
    }

    public void delete(long id) {
        this.resumeRepository.deleteById(id);
    }

    public ResFetchResumeDTO fetchResume(Resume resume) {
        ResFetchResumeDTO  resFetchResumeDTO = new ResFetchResumeDTO();
        resFetchResumeDTO.setId(resume.getId());
        resFetchResumeDTO.setEmail(resume.getEmail());
        resFetchResumeDTO.setUrl(resume.getUrl());
        resFetchResumeDTO.setCreatedAt(resume.getCreatedAt());
        resFetchResumeDTO.setCreatedBy(resume.getCreatedBy());
        resFetchResumeDTO.setUpdatedAt(resume.getUpdatedAt());
        resFetchResumeDTO.setUpdatedBy(resume.getUpdatedBy());
        resFetchResumeDTO.setUser(new ResFetchResumeDTO.UserResume(resume.getUser().getId(), resume.getUser().getName()));
        resFetchResumeDTO.setJob(new ResFetchResumeDTO.JobResume(resume.getJob().getId(), resume.getJob().getName()));

        return resFetchResumeDTO;
    }

    public Resume findById(Long id) {
        return this.resumeRepository.findById(id).get();
    }

    public ResultPaginationDTO fetchAll(Specification spec, Pageable pageable) {
        Page<Resume> pageResume = this.resumeRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageResume.getTotalPages());
        mt.setTotal(pageResume.getTotalElements());

        rs.setMeta(mt);

        List<ResFetchResumeDTO> listResume = pageResume.getContent().stream().map(this::fetchResume).toList();

        rs.setResult(listResume);

        return rs;
    }
}
