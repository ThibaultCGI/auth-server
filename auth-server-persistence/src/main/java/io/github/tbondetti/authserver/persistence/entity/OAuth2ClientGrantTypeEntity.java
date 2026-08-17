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
@Table(name = "oauth2_client_grant_type")
public class OAuth2ClientGrantTypeEntity {

    @EmbeddedId
    private OAuth2ClientGrantTypeId id;

    @MapsId("idOauth2Client")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oauth2_client")
    private OAuth2ClientEntity oauth2Client;
}
