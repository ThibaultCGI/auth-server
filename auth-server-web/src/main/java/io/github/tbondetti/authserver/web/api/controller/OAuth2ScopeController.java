package io.github.tbondetti.authserver.web.api.controller;

import io.github.tbondetti.authserver.web.api.dto.CreateOAuth2ScopeRequest;
import io.github.tbondetti.authserver.web.api.response.OAuth2ScopeResponse;
import io.github.tbondetti.authserver.web.facade.OAuth2ScopeFacade;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static io.github.tbondetti.authserver.web.api.mapper.OAuth2ScopeWebMapper.toResponse;


@RestController
@RequestMapping("/api/scopes")
@RequiredArgsConstructor
public class OAuth2ScopeController {

    private final OAuth2ScopeFacade oauth2ScopeFacade;

    @GetMapping("/{applicationCode}/{code}")
    public OAuth2ScopeResponse getOAuth2Scope(
            @PathVariable final String applicationCode,
            @PathVariable final String code
    ) {
        return toResponse(this.oauth2ScopeFacade.getOAuth2Scope(applicationCode, code));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public OAuth2ScopeResponse createOAuth2Scope(@RequestBody final CreateOAuth2ScopeRequest request) {
        return toResponse(this.oauth2ScopeFacade.createOAuth2Scope(
                request.applicationCode(),
                request.code(),
                request.name(),
                request.description()
        ));
    }
}
