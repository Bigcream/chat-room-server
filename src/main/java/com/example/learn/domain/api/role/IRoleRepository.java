package com.example.learn.domain.api.role;

import com.example.learn.infrastructure.database.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface IRoleRepository extends JpaRepository<RoleEntity, Long> {
    RoleEntity findByName(String role);
}
