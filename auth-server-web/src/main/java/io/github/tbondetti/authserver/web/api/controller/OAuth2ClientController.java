package io.github.tbondetti.authserver.web.api.controller;

import io.github.tbondetti.authserver.web.api.dto.AssignOAuth2ScopeRequest;
import io.github.tbondetti.authserver.web.api.dto.CreateOAuth2ClientRequest;
import io.github.tbondetti.authserver.web.api.response.CreateOAuth2ClientResponse;
import io.github.tbondetti.authserver.web.api.response.OAuth2ClientResponse;
import io.github.tbondetti.authserver.web.facade.OAuth2ClientFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static io.github.tbondetti.authserver.web.api.mapper.OAuth2ClientWebMapper.toCreateResponse;
import static io.github.tbondetti.authserver.web.api.mapper.OAuth2ClientWebMapper.toResponse;


@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class OAuth2ClientController {

    private final OAuth2ClientFacade oauth2ClientFacade;

    @GetMapping("/{clientId}")
    public OAuth2ClientResponse getOAuth2Client(@PathVariable final String clientId) {
        return toResponse(this.oauth2ClientFacade.getOAuth2Client(clientId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOAuth2ClientResponse createOAuth2Client(@RequestBody final CreateOAuth2ClientRequest request) {
        return toCreateResponse(this.oauth2ClientFacade.createOAuth2Client(
                request.clientName(),
                request.applicationCode()
        ));
    }

    @PostMapping("/{clientId}/scopes")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignScope(
            @PathVariable final String clientId,
            @RequestBody final AssignOAuth2ScopeRequest request
    ) {
        this.oauth2ClientFacade.assignScope(
                clientId,
                request.applicationCode(),
                request.code()
        );
    }
}
