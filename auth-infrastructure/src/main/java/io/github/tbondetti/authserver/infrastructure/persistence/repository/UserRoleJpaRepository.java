package io.github.tbondetti.authserver.infrastructure.persistence.repository;

import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserRoleEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserRoleId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserRoleJpaRepository extends JpaRepository<UserRoleEntity, UserRoleId> {

    @Query(
            """
            SELECT
                ur.role
            FROM UserRoleEntity ur
            JOIN FETCH ur.role.application a
            WHERE ur.user.username = :username
                AND a.code = :applicationCode
            """
    )
    List<RoleEntity> findAllByUsernameAndApplicationCode(
            @Param("username") final String username,
            @Param("applicationCode") final String applicationCode
    );


    @Query(
            """
            SELECT
                ur.role
            FROM UserRoleEntity ur
            JOIN FETCH ur.role.application a
            WHERE ur.role.code = :roleCode
                AND ur.user.username = :username
                AND a.code = :applicationCode
            """
    )
    Optional<RoleEntity> findByCodeAndApplicationCodeAndUsername(
            @Param("roleCode") final String roleCode,
            @Param("applicationCode") final String applicationCode,
            @Param("username") final String username
    );

    @Query(
            """
            SELECT
                ur.role
            FROM UserRoleEntity ur
            JOIN FETCH ur.role.application a
            WHERE ur.user.username = :username
            """
    )
    List<RoleEntity> findAllByUsername(@Param("user") final String username);

}
