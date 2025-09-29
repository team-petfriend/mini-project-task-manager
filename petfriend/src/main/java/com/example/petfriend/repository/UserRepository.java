package com.example.petfriend.repository;

import com.example.petfriend.entity.User;
import jakarta.validation.constraints.*;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    @EntityGraph(attributePaths = "userRoles")
    Optional<User> findByLoginId(String loginId);

    boolean existsByLoginId(@NotBlank @Size(min = 4, max = 50) String s);
    boolean existsByEmail(@NotBlank @Email @Size(max = 50) String email);
    boolean existsByNickname(@NotBlank @Size(max = 50) String nickname);

    @EntityGraph(attributePaths = "userRoles")
    Optional<User> findWithRolesById(
            @NotNull(message = "userId는 필수입니다.")
            @Positive(message = "userId는 양수여야 합니다.")
            Long id
    );
}
