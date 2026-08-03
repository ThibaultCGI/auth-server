package io.github.tbondetti.authserver.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "oauth2_scope")
public class OAuth2ScopeEntity {

    @Id
    @Column(
            name = "id_oauth2_scope",
            nullable = false
    )
    private UUID id;

    @Column(
            name = "code",
            nullable = false,
            length = 100
    )
    private String code;

    @Column(
            name = "name",
            nullable = false,
            length = 100
    )
    private String name;

    @Column(
            name = "description",
            length = 500
    )
    private String description;

    @JoinColumn(
            name = "id_application",
            nullable = false
    )
    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationEntity application;
}
