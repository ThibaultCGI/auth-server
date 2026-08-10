package io.github.tbondetti.authserver.persistence.entity;

import io.github.tbondetti.authserver.core.enums.OAuth2ClientGrantType;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
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
public class OAuth2ClientGrantTypeId implements Serializable {

    @Serial
    private static final long serialVersionUID = -1556005282139322519L;

    @Column(
            name = "id_oauth2_client",
            nullable = false
    )
    private UUID idOauth2Client;

    @Enumerated(EnumType.STRING)
    @Column(
            name = "grant_type",
            nullable = false
    )
    private OAuth2ClientGrantType grantType;
}
