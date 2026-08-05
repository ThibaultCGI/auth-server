package io.github.tbondetti.authserver.web.api.v1.controller;

import io.github.tbondetti.authserver.application.service.OAuth2ScopeService;
import io.github.tbondetti.authserver.openapi.administration.api.OAuth2ScopeApi;
import io.github.tbondetti.authserver.openapi.administration.dto.CreateOAuth2ScopeRequestApi;
import io.github.tbondetti.authserver.openapi.administration.response.OAuth2ScopeResponseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static io.github.tbondetti.authserver.web.api.v1.mapper.OAuth2ScopeWebMapper.toResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;


@RestController
@RequestMapping("/api/v1/scopes")
@RequiredArgsConstructor
public class OAuth2ScopeController implements OAuth2ScopeApi {

    private final OAuth2ScopeService oauth2ScopeService;

    @GetMapping(
            value = "/{applicationCode}/{code}",
            produces = APPLICATION_JSON_VALUE
    )
    public OAuth2ScopeResponseApi getOAuth2Scope(
            @PathVariable final String applicationCode,
            @PathVariable final String code
    ) {
        return toResponse(this.oauth2ScopeService.getOAuth2Scope(applicationCode, code));
    }

    @PostMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public OAuth2ScopeResponseApi createOAuth2Scope(@RequestBody final CreateOAuth2ScopeRequestApi request) {
        return toResponse(this.oauth2ScopeService.createOAuth2Scope(
                request.applicationCode(),
                request.code(),
                request.name(),
                request.description()
        ));
    }
}
