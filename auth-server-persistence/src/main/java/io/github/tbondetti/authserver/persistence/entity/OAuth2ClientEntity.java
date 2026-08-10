package io.github.tbondetti.authserver.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Set;
import java.util.UUID;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "oauth2_client")
public class OAuth2ClientEntity {

    @Id
    @Column(
            name = "id_oauth2_client",
            nullable = false
    )
    private UUID id;

    @Column(
            name = "client_id",
            nullable = false,
            unique = true,
            length = 100
    )
    private String clientId;

    @Column(
            name = "client_name",
            nullable = false
    )
    private String clientName;

    @Column(
            name = "client_secret",
            nullable = false
    )
    private String clientSecret;

    @OneToMany(
            mappedBy = "id.idOauth2Client"
    )
    private Set<OAuth2ClientGrantTypeEntity> grantTypes;

    @OneToMany(
            mappedBy = "id.idOauth2Client"
    )
    private Set<OAuth2ClientRedirectUriEntity> redirectUris;


    @JoinColumn(
            name = "id_application",
            nullable = false
    )
    @ManyToOne(fetch = FetchType.LAZY)
    private ApplicationEntity application;
}
