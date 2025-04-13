package com.minhdev.project.controller;

import com.minhdev.project.domain.Permission;
import com.minhdev.project.domain.response.ResultPaginationDTO;
import com.minhdev.project.service.PermissionService;
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
public class PermissionController {
    private final PermissionService permissionService;

    public PermissionController(PermissionService permissionService) {
        this.permissionService = permissionService;
    }

    @PostMapping("/permissions")
    @ApiMessage("Create a permission")
    public ResponseEntity<Permission> createPermission(@Valid @RequestBody Permission permission) throws CustomizeException {
        if (this.permissionService.isPermissionExists(permission)) {
            throw new CustomizeException("Permission already exists");
        }
        return ResponseEntity.status(HttpStatus.CREATED).body(this.permissionService.create(permission));
    }

    @PutMapping("/permissions")
    @ApiMessage("Update a permission")
    public ResponseEntity<Permission> updatePermission(@Valid @RequestBody Permission permission) throws CustomizeException {
        if (this.permissionService.fetchById(permission.getId()) == null) {
            throw new CustomizeException("Permission does not exist");
        }

        if (this.permissionService.isPermissionExists(permission)) {
            throw new CustomizeException("Permission already exists");
        }
        return ResponseEntity.ok().body(this.permissionService.update(permission));
    }

    @DeleteMapping("/permissions/{id}")
    @ApiMessage("Delete a permission")
    public ResponseEntity<Void> deletePermission(@PathVariable Long id) throws CustomizeException {
        if (this.permissionService.fetchById(id) == null) {
            throw new CustomizeException("Permission does not exist");
        }
        this.permissionService.delete(id);
        return ResponseEntity.ok().body(null);
    }

    @GetMapping("/permissions")
    @ApiMessage("Fetch permissons")
    public ResponseEntity<ResultPaginationDTO> getPermissons(
            @Filter Specification<Permission> spec,
            Pageable pageable){
        return ResponseEntity.ok().body(this.permissionService.fetchAll(spec, pageable));
    }
}
