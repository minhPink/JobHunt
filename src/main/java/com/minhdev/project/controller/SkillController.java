package com.minhdev.project.controller;

import com.minhdev.project.domain.Skill;
import com.minhdev.project.domain.response.ResultPaginationDTO;
import com.minhdev.project.service.SkillService;
import com.minhdev.project.util.annotation.ApiMessage;
import com.minhdev.project.util.error.CustomizeException;
import com.turkraft.springfilter.boot.Filter;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class SkillController {
    private final SkillService skillService;

    public SkillController(SkillService skillService) {
        this.skillService = skillService;
    }

    @PostMapping("/skills")
    @ApiMessage("Create a skill")
    public ResponseEntity<Skill> create(@Valid @RequestBody Skill skill) throws CustomizeException {
        if (skill.getName() != null && this.skillService.isNameExists(skill.getName())) {
            throw new CustomizeException("Skill with name " + skill.getName() + " already exists");
        };
        return ResponseEntity.status(HttpStatus.CREATED).body(this.skillService.create(skill));
    }

    @PutMapping("/skills")
    @ApiMessage("Update a skill")
    public ResponseEntity<Skill> update(@Valid @RequestBody Skill skill) throws CustomizeException {
        Skill existsSkill = this.skillService.fetchSkillById(skill.getId());
        if (existsSkill == null) {
            throw new CustomizeException("Skill with id " + skill.getId() + " does not exist");
        };

        if (existsSkill.getName() != null && this.skillService.isNameExists(skill.getName())) {
            throw new CustomizeException("Skill with name " + existsSkill.getName() + " already exists");
        }
        existsSkill.setName(skill.getName());
        return ResponseEntity.ok(this.skillService.update(existsSkill));
    }

    @GetMapping("/skills")
    @ApiMessage("Fetch all skills")
    public ResponseEntity<ResultPaginationDTO> getAll(
            @Filter Specification<Skill> spec,
            Pageable pageable
            ) {
        return ResponseEntity.status(HttpStatus.OK).body(this.skillService.getAllSkills(spec, pageable));
    }

    @DeleteMapping("/skills/{id}")
    @ApiMessage("Delete a skill")
    public ResponseEntity<Void> delete(@PathVariable Long id) throws CustomizeException {
        Skill skill = this.skillService.fetchSkillById(id);
        if (skill == null) {
            throw new CustomizeException("Skill with id " + id + " does not exist");
        }
        this.skillService.deleteSkillById(id);
        return ResponseEntity.ok().body(null);
    }
}
