package io.github.tbondetti.authserver.web.api.v1.controller;

import io.github.tbondetti.authserver.application.service.OAuth2ClientService;
import io.github.tbondetti.authserver.web.openapi.api.OAuth2ClientApi;
import io.github.tbondetti.authserver.web.openapi.dto.AssignOAuth2ScopeRequestApi;
import io.github.tbondetti.authserver.web.openapi.dto.CreateOAuth2ClientRequestApi;
import io.github.tbondetti.authserver.web.openapi.response.CreateOAuth2ClientResponseApi;
import io.github.tbondetti.authserver.web.openapi.response.OAuth2ClientResponseApi;
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
public class OAuth2ClientController implements OAuth2ClientApi {

    private final OAuth2ClientService oauth2ClientService;

    @GetMapping(
            value = "/{clientId}",
            produces = APPLICATION_JSON_VALUE
 )
    public OAuth2ClientResponseApi getOAuth2Client(@PathVariable final String clientId) {
        return toResponse(this.oauth2ClientService.getOAuth2Client(clientId));
    }

    @PostMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOAuth2ClientResponseApi createOAuth2Client(@RequestBody final CreateOAuth2ClientRequestApi request) {
        return toCreateResponse(this.oauth2ClientService.createOAuth2Client(
                request.clientName(),
                request.applicationCode()
        ));
    }

    @PostMapping(
            value = "/{clientId}/scopes",
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void assignScope(
            @PathVariable final String clientId,
            @RequestBody final AssignOAuth2ScopeRequestApi request
    ) {
        this.oauth2ClientService.assignScope(
                clientId,
                request.applicationCode(),
                request.code()
        );
    }
}
