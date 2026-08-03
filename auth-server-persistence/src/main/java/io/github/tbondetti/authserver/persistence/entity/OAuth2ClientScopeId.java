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
public class OAuth2ClientScopeId implements Serializable {

    @Serial
    private static final long serialVersionUID = -4540547123414892803L;

    @Column(name = "id_oauth2_client", nullable = false)
    private UUID idClient;

    @Column(name = "id_oauth2_scope", nullable = false)
    private UUID idScope;
}
