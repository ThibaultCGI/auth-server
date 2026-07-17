package io.github.tbondetti.authserver.infrastructure.persistence.repository;

import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.UserEntity;
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
                r
            FROM UserRoleEntity ur
            JOIN ur.role r
            WHERE ur.user = :user
                AND r.application = :application
            """
    )
    List<RoleEntity> findAllByUserAndApplication(@Param("user") final UserEntity userEntity,
                                                 @Param("application") final ApplicationEntity applicationEntity);


    @Query(
            """
            SELECT r
            FROM UserRoleEntity ur
            JOIN ur.role r
            WHERE r.code = :roleCode
                AND ur.user = :user
                AND r.application = :application
            """
    )
    Optional<RoleEntity> findByCodeAndApplicationAndUser(
            @Param("roleCode") final String roleCode,
            @Param("application") final ApplicationEntity applicationEntity,
            @Param("user") final UserEntity userEntity
    );
}
