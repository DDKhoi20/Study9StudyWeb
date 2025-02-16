package com.example.Study9StudyWeb.repository;

import com.example.Study9StudyWeb.entity.RoleEntity;
import com.example.Study9StudyWeb.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByRole(Role role);
}
