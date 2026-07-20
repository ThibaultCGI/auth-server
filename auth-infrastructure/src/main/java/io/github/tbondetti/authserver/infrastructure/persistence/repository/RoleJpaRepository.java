package io.github.tbondetti.authserver.infrastructure.persistence.repository;

import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.RoleEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;
import java.util.UUID;

public interface RoleJpaRepository extends JpaRepository<RoleEntity, UUID> {


    /* on force le chargement de l'application (qui est lazy sinon) */
    @Query(
        """
        SELECT
            r
        FROM RoleEntity r
        JOIN FETCH r.application
        WHERE r.code = :code
            AND r.application = :application
        """
    )
    RoleEntity getByApplicationAndCode(
            @Param("application") final ApplicationEntity application,
            @Param("code") final String code
    );

    /* on force le chargement de l'application (qui est lazy sinon) */
    @Query(
            """
            SELECT
                r
            FROM RoleEntity r
            JOIN FETCH r.application
            WHERE r.code = :code
                AND r.application = :application
            """
    )
    Optional<RoleEntity> findByCodeAndApplication(
            @Param("code") final String code,
            @Param("application") final ApplicationEntity application
    );

}
