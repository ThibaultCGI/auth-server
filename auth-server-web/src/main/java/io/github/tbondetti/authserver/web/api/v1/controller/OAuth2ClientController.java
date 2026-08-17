package io.github.tbondetti.authserver.web.api.v1.controller;

import io.github.tbondetti.authserver.application.service.OAuth2ClientService;
import io.github.tbondetti.authserver.openapi.administration.api.OAuth2ClientApi;
import io.github.tbondetti.authserver.web.api.v1.dto.AssignOAuth2ScopeRequest;
import io.github.tbondetti.authserver.web.api.v1.dto.CreateOAuth2ClientRequest;
import io.github.tbondetti.authserver.web.api.v1.response.CreateOAuth2ClientResponse;
import io.github.tbondetti.authserver.web.api.v1.response.OAuth2ClientResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static io.github.tbondetti.authserver.web.api.v1.mapper.OAuth2ClientWebMapper.toCreateResponse;
import static io.github.tbondetti.authserver.web.api.v1.mapper.OAuth2ClientWebMapper.toResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/api/v1/clients")
@RequiredArgsConstructor
public class OAuth2ClientController implements OAuth2ClientApi<CreateOAuth2ClientRequest, AssignOAuth2ScopeRequest> {

    private final OAuth2ClientService oauth2ClientService;

    @Override
    @GetMapping(
            value = "/{clientId}",
            produces = APPLICATION_JSON_VALUE
 )
    public OAuth2ClientResponse getOAuth2Client(@PathVariable final String clientId) {
        return toResponse(this.oauth2ClientService.getOAuth2Client(clientId));
    }

    @Override
    @PostMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOAuth2ClientResponse createOAuth2Client(@Valid @RequestBody final CreateOAuth2ClientRequest request) {
        return toCreateResponse(this.oauth2ClientService.createOAuth2Client(
                request.clientName(),
                request.applicationCode(),
                request.grantTypes(),
                request.redirectUris()
        ));
    }

    @Override
    @PostMapping(
            value = "/{clientId}/scopes",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignScope(
            @PathVariable final String clientId,
            @Valid @RequestBody final AssignOAuth2ScopeRequest request
    ) {
        this.oauth2ClientService.assignScope(
                clientId,
                request.applicationCode(),
                request.code()
        );
    }
}
