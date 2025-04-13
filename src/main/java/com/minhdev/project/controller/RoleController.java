package com.minhdev.project.controller;

import com.minhdev.project.domain.Role;
import com.minhdev.project.domain.response.ResultPaginationDTO;
import com.minhdev.project.service.RoleService;
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
public class RoleController {
    private final RoleService roleService;

    public RoleController(RoleService roleService) {
        this.roleService = roleService;
    }

    @PostMapping("/roles")
    @ApiMessage("Create a role")
    public ResponseEntity<Role> createRole(@Valid @RequestBody Role role) throws CustomizeException {
        if (this.roleService.existsByName(role.getName())) {
            throw new CustomizeException("Role already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.roleService.create(role));
    }

    @PutMapping("/roles")
    @ApiMessage("Update a role")
    public ResponseEntity<Role> updateRole(@Valid @RequestBody Role role) throws CustomizeException {
        if (this.roleService.existsRoles(role.getId()) == false) {
            throw new CustomizeException("Role does not exist");
        }

        if (this.roleService.existsByName(role.getName())) {
            throw new CustomizeException("Role already exists");
        }

        return ResponseEntity.ok().body(this.roleService.update(role));
    }

    @DeleteMapping("/roles")
    @ApiMessage("Delete a role")
    public ResponseEntity<Void> deleteRole(@Valid @PathVariable Long id) throws CustomizeException {
        if (this.roleService.existsRoles(id) == false) {
            throw new CustomizeException("Role does not exist");
        }
        this.roleService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/roles")
    @ApiMessage("Fetch roles")
    public ResponseEntity<ResultPaginationDTO> getRoles(
            @Filter Specification<Role> spec,
            Pageable pageable) {
        return ResponseEntity.ok().body(this.roleService.fetchRoles(spec, pageable));
    }
}
