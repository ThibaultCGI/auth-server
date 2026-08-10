package io.github.tbondetti.authserver.persistence.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serial;
import java.io.Serializable;
import java.util.UUID;


@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Embeddable
public class OAuth2ClientRedirectUriId implements Serializable {

    @Serial
    private static final long serialVersionUID = -8735808510605003558L;

    @Column(
            name = "id_oauth2_client",
            nullable = false
    )
    private UUID idOauth2Client;

    @Column(
            name = "redirect_uri",
            nullable = false
    )
    private String redirectUri;
}
