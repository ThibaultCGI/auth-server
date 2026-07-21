package io.github.tbondetti.authserver.infrastructure.persistence.repository;

import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, UUID> {

    /* on force le chargement de l'application (qui est lazy sinon) */
    String FIND_BY_APPLICATION_AND_CODE_QUERY =
            """
            SELECT
                r
            FROM RoleEntity r
            JOIN FETCH r.application
            WHERE r.code = :code
                AND r.application.code = :applicationCode
            """;

    @Query(FIND_BY_APPLICATION_AND_CODE_QUERY)
    RoleEntity getByApplicationCodeAndCode(
            @Param("applicationCode") final String applicationCode,
            @Param("code") final String code
    );

    @Query(FIND_BY_APPLICATION_AND_CODE_QUERY)
    Optional<RoleEntity> findByApplicationCodeAndCode(
            @Param("applicationCode") final String applicationCode,
            @Param("code") final String code
    );

}
