package io.github.tbondetti.authserver.persistence.entity;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.MapsId;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "oauth2_client_redirect_uri")
public class OAuth2ClientRedirectUriEntity {

    @EmbeddedId
    private OAuth2ClientRedirectUriId id;

    @MapsId("idOauth2Client")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "id_oauth2_client")
    private OAuth2ClientEntity oauth2Client;

}
