package com.minhdev.project.controller;

import com.minhdev.project.domain.Resume;
import com.minhdev.project.domain.response.ResultPaginationDTO;
import com.minhdev.project.domain.response.resume.ResCreateResumeDTO;
import com.minhdev.project.domain.response.resume.ResFetchResumeDTO;
import com.minhdev.project.domain.response.resume.ResUpdateResumeDTO;
import com.minhdev.project.service.ResumeService;
import com.minhdev.project.util.annotation.ApiMessage;
import com.minhdev.project.util.error.CustomizeException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/v1")
public class ResumeController {
    private final ResumeService resumeService;

    public ResumeController(ResumeService resumeService) {
        this.resumeService = resumeService;
    }

    @PostMapping("/resumes")
    @ApiMessage("Create a resume")
    public ResponseEntity<ResCreateResumeDTO> createResume(@Valid @RequestBody Resume resume) throws CustomizeException {
        boolean existsResume = this.resumeService.checkResumeExistsByUserAndJob(resume);
        if (existsResume == false) {
           throw new CustomizeException("Resume with id/job not found");
       }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.resumeService.create(resume));
    }

    @PutMapping("/resumes")
    @ApiMessage("Update a reusme")
    public ResponseEntity<ResUpdateResumeDTO> updateResume(@RequestBody Resume resume) throws CustomizeException {
        Optional<Resume> resumeOptional = Optional.ofNullable(this.resumeService.findById(resume.getId()));
        if (resumeOptional.isEmpty()){
            throw new CustomizeException("Resume with id/job not found");
        }
        Resume resumeToUpdate = resumeOptional.get();
        resumeToUpdate.setStatus(resume.getStatus());

        return ResponseEntity.ok().body(this.resumeService.update(resumeToUpdate));
    }

    @DeleteMapping("/resumes/{id}")
    @ApiMessage("Delete a resume by id")
    public ResponseEntity<Void> deleteResume(@PathVariable Long id) throws CustomizeException {
        Optional<Resume> resumeOptional = Optional.ofNullable(this.resumeService.findById(id));
        if (resumeOptional.isEmpty()){
            throw new CustomizeException("Resume with id not found");
        }

        this.resumeService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/resumes/{id}")
    @ApiMessage("Fetch a resume by id")
    public ResponseEntity<ResFetchResumeDTO> findById(@PathVariable Long id) throws CustomizeException {
        Optional<Resume> resumeOptional = Optional.ofNullable(this.resumeService.findById(id));
        if (resumeOptional.isEmpty()) {
            throw new CustomizeException("Resume with id " + id + " not found");
        }

        return ResponseEntity.ok().body(this.resumeService.fetchResume(resumeOptional.get()));
    }

    @GetMapping("/resumes")
    @ApiMessage("Fetch all resume with pagination")
    public ResponseEntity<ResultPaginationDTO> fetchAll(
            @Filter Specification<Resume>  spec,
            Pageable pageable
            ) {
        return ResponseEntity.ok().body(this.resumeService.fetchAll(spec, pageable));
    }
}
