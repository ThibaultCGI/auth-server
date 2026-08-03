package io.github.tbondetti.authserver.infrastructure.persistence.entity;

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
public class UserRoleId implements Serializable {

    @Serial
    private static final long serialVersionUID = 9080570502262161065L;

    @Column(name = "id_iam_user", nullable = false)
    private UUID idIamUser;

    @Column(name = "id_role", nullable = false)
    private UUID idRole;
}
