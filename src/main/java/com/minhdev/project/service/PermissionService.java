package com.minhdev.project.service;

import com.minhdev.project.domain.Permission;
import com.minhdev.project.domain.Resume;
import com.minhdev.project.domain.response.ResultPaginationDTO;
import com.minhdev.project.domain.response.resume.ResFetchResumeDTO;
import com.minhdev.project.repository.PermissionRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PermissionService {
    private final PermissionRepository permissionRepository;

    public PermissionService(PermissionRepository permissionRepository) {
        this.permissionRepository = permissionRepository;
    }

    public boolean isPermissionExists(Permission permission) {
        return permissionRepository.existsByModuleAndApiPathAndMethod(
                permission.getModule(),
                permission.getApiPath(),
                permission.getMethod()
        );
    }

    public Permission create(Permission permission) {
        return permissionRepository.save(permission);
    }

    public Permission fetchById(Long id) {
        return permissionRepository.findById(id).orElse(null);
    }

    public Permission update(Permission permission) {
        Permission permissionToUpdate = this.fetchById(permission.getId());
        if (permissionToUpdate != null) {
            permissionToUpdate.setName(permission.getName());
            permissionToUpdate.setApiPath(permission.getApiPath());
            permissionToUpdate.setMethod(permission.getMethod());
            permissionToUpdate.setModule(permission.getModule());

            permissionToUpdate = this.permissionRepository.save(permissionToUpdate);
            return permissionToUpdate;
        }
        return null;
    }

    public void delete(Long id) {
        Optional<Permission> permissionToDelete = this.permissionRepository.findById(id);
        Permission deletedPermission = permissionToDelete.get();
        deletedPermission.getRoles().forEach(role -> role.getPermissions().remove(deletedPermission));

        this.permissionRepository.delete(deletedPermission);
    }

    public ResultPaginationDTO fetchAll(Specification spec, Pageable pageable) {
        Page<Permission> pagePermissions = this.permissionRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pagePermissions.getTotalPages());
        mt.setTotal(pagePermissions.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pagePermissions.getContent());

        return rs;
    }

    public boolean isSameName(Permission permission) {
        Permission permissionToCheck = this.fetchById(permission.getId());
        if (permissionToCheck != null) {
            if (permissionToCheck.getName().equals(permission.getName())) {
                return true;
            }
        }
        return false;
    }
}
