package com.minhdev.project.service;

import com.minhdev.project.domain.Permission;
import com.minhdev.project.domain.Role;
import com.minhdev.project.domain.response.ResultPaginationDTO;
import com.minhdev.project.repository.PermissionRepository;
import com.minhdev.project.repository.RoleRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class RoleService {
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;

    public RoleService(RoleRepository roleRepository, PermissionRepository permissionRepository) {
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
    }

    public boolean existsByName(String name){
        return this.roleRepository.existsByName(name);
    }

    public Role create(Role role){
        if (role.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions()
                    .stream().map(Permission::getId)
                    .collect(Collectors.toList());
            List<Permission> dbPermissions = this.permissionRepository.findAllById(reqPermissions);
            role.setPermissions(dbPermissions);
        }

        return this.roleRepository.save(role);
    }

    public boolean existsRoles(Long id){
        return this.roleRepository.existsById(id);
    }

    public Role update(Role role){
        Role roleToUpdate = this.roleRepository.findById(role.getId()).get();

        if (roleToUpdate.getPermissions() != null) {
            List<Long> reqPermissions = role.getPermissions()
                    .stream().map(x -> x.getId())
                    .collect(Collectors.toList());

            List<Permission> dbPermissions = this.permissionRepository.findByIdIn(reqPermissions);
            role.setPermissions(dbPermissions);
        }

        roleToUpdate.setName(role.getName());
        roleToUpdate.setDescription(role.getDescription());
        roleToUpdate.setActive(role.isActive());
        roleToUpdate.setPermissions(role.getPermissions());
        roleToUpdate = this.roleRepository.save(roleToUpdate);
        return roleToUpdate;
    }

    public void delete(Long id){
        this.roleRepository.deleteById(id);
    }

    public ResultPaginationDTO fetchRoles(Specification spec,Pageable pageable){
        Page<Role> pageRoles = this.roleRepository.findAll(spec, pageable);
        ResultPaginationDTO rs = new ResultPaginationDTO();
        ResultPaginationDTO.Meta mt = new ResultPaginationDTO.Meta();

        mt.setPage(pageable.getPageNumber() + 1);
        mt.setPageSize(pageable.getPageSize());
        mt.setPages(pageRoles.getTotalPages());
        mt.setTotal(pageRoles.getTotalElements());

        rs.setMeta(mt);
        rs.setResult(pageRoles.getContent());

        return rs;
    }
}
