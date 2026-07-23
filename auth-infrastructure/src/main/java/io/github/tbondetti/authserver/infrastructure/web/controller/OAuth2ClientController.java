package io.github.tbondetti.authserver.infrastructure.web.controller;

import io.github.tbondetti.authserver.infrastructure.service.OAuth2ClientService;
import io.github.tbondetti.authserver.infrastructure.web.dto.CreateOAuth2ClientRequest;
import io.github.tbondetti.authserver.infrastructure.web.response.CreateOAuth2ClientResponse;
import io.github.tbondetti.authserver.infrastructure.web.response.OAuth2ClientResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static io.github.tbondetti.authserver.infrastructure.web.mapper.OAuth2ClientWebMapper.toCreateResponse;
import static io.github.tbondetti.authserver.infrastructure.web.mapper.OAuth2ClientWebMapper.toResponse;


@RestController
@RequestMapping("/api/clients")
@RequiredArgsConstructor
public class OAuth2ClientController {

    private final OAuth2ClientService oauth2ClientService;

    @GetMapping("/{clientId}")
    public OAuth2ClientResponse getOAuth2Client(@PathVariable final String clientId) {
        return toResponse(this.oauth2ClientService.getOAuth2Client(clientId));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateOAuth2ClientResponse createOAuth2Client(@RequestBody final CreateOAuth2ClientRequest request) {
        return toCreateResponse(this.oauth2ClientService.createOAuth2Client(
                request.clientName(),
                request.applicationCode()
        ));
    }
}
