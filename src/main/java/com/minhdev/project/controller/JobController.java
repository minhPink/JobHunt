package com.minhdev.project.controller;

import com.minhdev.project.domain.Job;
import com.minhdev.project.domain.response.ResCreateJobDTO;
import com.minhdev.project.domain.response.ResUpdateJobDTO;
import com.minhdev.project.domain.response.ResultPaginationDTO;
import com.minhdev.project.service.JobService;
import com.minhdev.project.util.annotation.ApiMessage;
import com.minhdev.project.util.error.CustomizeException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class JobController {
    private final JobService jobService;

    public JobController(JobService jobService) {
        this.jobService = jobService;
    }

    @PostMapping("/jobs")
    @ApiMessage("Create a job")
    public ResponseEntity<ResCreateJobDTO> createJob(@Valid @RequestBody Job job) {
        return ResponseEntity.status(HttpStatus.CREATED).body(this.jobService.create(job));
    }

    @PutMapping("/jobs")
    @ApiMessage("Update a job")
    public ResponseEntity<ResUpdateJobDTO> updateJob(@Valid @RequestBody Job job) throws CustomizeException {
        Optional<Job> currentJob = Optional.ofNullable(this.jobService.fetchJbById(job.getId()));
        if (!currentJob.isPresent()) {
            throw new CustomizeException("Job not found");
        }
        return ResponseEntity.ok().body(this.jobService.update(job));
    }

    @DeleteMapping("/jobs/{id}")
    @ApiMessage("Delete a job")
    public ResponseEntity<Void> deleteJob(@PathVariable long id) throws CustomizeException {
        Optional<Job> currentJob = Optional.ofNullable(this.jobService.fetchJbById(id));
        if (!currentJob.isPresent()) {
            throw new CustomizeException("Job not found");
        };
        this.jobService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/jobs/{id}")
    @ApiMessage("Get a job")
    public ResponseEntity<Job> getJob(@PathVariable long id) throws CustomizeException {
        Optional<Job> currentJob = Optional.ofNullable(this.jobService.fetchJbById(id));
        if (!currentJob.isPresent()) {
            throw new CustomizeException("Job not found");
        }
        return ResponseEntity.ok().body(currentJob.get());
    }

    @GetMapping("/jobs")
    @ApiMessage("Get jobs")
    public ResponseEntity<ResultPaginationDTO> getAllJob(
            @Filter Specification<Job> spec,
            Pageable pageable
            ) {
        return ResponseEntity.ok().body(this.jobService.getAllJobs(spec, pageable));
    }
}
