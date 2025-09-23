package com.example.petfriend.entity;

import com.example.petfriend.common.enums.RoleType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "user_roles")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserRole {

    @EmbeddedId
    private UserRoleId id;

    @MapsId("userId")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn
            (
                    name = "user_id",
                    nullable = false,
                    foreignKey = @ForeignKey(name = "fk_user_roles_user")
            )
    private User user;

    @MapsId("roleName")
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "role_name",
            nullable = false,
            referencedColumnName = "role_name",
            foreignKey = @ForeignKey(name = "fk_user_roles_role" )
    )
    private Role role;


    public UserRole(User user, Role role) {
        this.user = user;
        this.role = role;
        Long userId = user.getId();
        RoleType roleName = role.getName();
        this.id = new UserRoleId(userId, roleName);
    }
}
