package com.example.petfriend.repository;

import com.example.petfriend.common.enums.RoleType;
import com.example.petfriend.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, RoleType> {
}
