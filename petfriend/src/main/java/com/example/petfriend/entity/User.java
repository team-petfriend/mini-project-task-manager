package com.example.petfriend.entity;

import com.example.petfriend.common.enums.Gender;
import com.example.petfriend.common.enums.RoleType;
import com.example.petfriend.entity.base.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Entity
@Table(
        name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_login_id", columnNames = "login_id"),
                @UniqueConstraint(name = "uk_users_email", columnNames = "email"),
                @UniqueConstraint(name = "uk_users_nickname", columnNames = "nickname")
        }
)
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class User extends BaseTimeEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", updatable = false)
    private Long id;

    @Column(name = "login_id", updatable = false, nullable = false, length = 50)
    private String loginId;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "email", nullable = false, length = 255)
    private String email;

    @Column(name = "nickname", nullable = false, length = 50)
    private String nickname;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender", length = 20)
    private Gender gender;

    @OneToMany(mappedBy = "assignessUser", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskAssignees> assignees = new HashSet<>();

    @OneToMany(mappedBy = "commenter", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<Comments> userComment = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserRole> userRoles = new HashSet<>();

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TaskHistory> taskHistories = new HashSet<>();

    @Builder
    private User(String loginId, String password, String email, String nickname, Gender gender) {
        this.loginId = loginId;
        this.password = password;
        this.email = email;
        this.nickname = nickname;
        this.gender = gender;
    }

    public void changeProfile(String nickname, Gender gender) {
        this.nickname = nickname;
        this.gender = gender;
    }

    public void grantRole(Role role) {
        boolean exists = userRoles.stream().anyMatch(ur -> ur.getRole().equals(role));

        if (!exists) {
            userRoles.add(new UserRole(this, role));
        }
    }

    public void revokeRole(Role role) {
        userRoles.removeIf(ur -> ur.getRole().equals(role));
    }

    public Set<RoleType> getRoleTypes() {
        return userRoles.stream()
                .map(ur -> ur.getRole().getName())
                .collect(Collectors.toUnmodifiableSet());
    }

}

