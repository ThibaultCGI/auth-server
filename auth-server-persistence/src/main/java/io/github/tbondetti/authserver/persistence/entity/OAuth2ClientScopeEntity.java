package io.github.tbondetti.authserver.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "oauth2_client_oauth2_scope")
public class OAuth2ClientScopeEntity {

    @EmbeddedId
    private OAuth2ClientScopeId id;

    @MapsId("idClient")
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "id_oauth2_client",
            nullable = false
    )
    private OAuth2ClientEntity client;

    @MapsId("idScope")
    @ManyToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(
            name = "id_oauth2_scope",
            nullable = false
    )
    private OAuth2ScopeEntity scope;
}