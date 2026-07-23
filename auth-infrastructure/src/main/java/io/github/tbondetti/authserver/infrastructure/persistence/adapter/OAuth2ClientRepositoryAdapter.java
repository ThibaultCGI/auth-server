package io.github.tbondetti.authserver.infrastructure.persistence.adapter;

import io.github.tbondetti.authserver.core.domain.OAuth2Client;
import io.github.tbondetti.authserver.core.port.OAuth2ClientRepositoryPort;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.ApplicationEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.entity.OAuth2ClientEntity;
import io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ClientMapper;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.ApplicationJpaRepository;
import io.github.tbondetti.authserver.infrastructure.persistence.repository.OAuth2ClientJpaRepository;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ClientMapper.toDomain;
import static io.github.tbondetti.authserver.infrastructure.persistence.mapper.OAuth2ClientMapper.toEntity;

@RequiredArgsConstructor
public class OAuth2ClientRepositoryAdapter implements OAuth2ClientRepositoryPort {

    private final OAuth2ClientJpaRepository oauth2ClientJpaRepository;
    private final ApplicationJpaRepository applicationJpaRepository;

    @Override
    public Optional<OAuth2Client> findByClientId(final String clientId) {
        return this.oauth2ClientJpaRepository.findByClientId(clientId).map(OAuth2ClientMapper::toDomain);
    }

    @Override
    public OAuth2Client save(final OAuth2Client client) {
        final ApplicationEntity application = this.applicationJpaRepository.getByCode(client.applicationCode());
        final OAuth2ClientEntity toSave = toEntity(client, application);
        final OAuth2ClientEntity saved = this.oauth2ClientJpaRepository.save(toSave);

        return toDomain(this.oauth2ClientJpaRepository.getByClientId(saved.getClientId()));
    }
}
