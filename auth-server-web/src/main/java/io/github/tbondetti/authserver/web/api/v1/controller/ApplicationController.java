package io.github.tbondetti.authserver.web.api.v1.controller;

import io.github.tbondetti.authserver.application.service.ApplicationService;
import io.github.tbondetti.authserver.web.openapi.api.ApplicationApi;
import io.github.tbondetti.authserver.web.openapi.dto.CreateApplicationRequestApi;
import io.github.tbondetti.authserver.web.openapi.response.ApplicationResponseApi;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static io.github.tbondetti.authserver.web.api.v1.mapper.ApplicationWebMapper.toResponse;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;

@RestController
@RequestMapping("/api/v1/applications")
@RequiredArgsConstructor
public class ApplicationController implements ApplicationApi {

    private final ApplicationService applicationService;

    @GetMapping(
            value = "/{code}",
            produces = APPLICATION_JSON_VALUE
    )
    public ApplicationResponseApi getApplication(@PathVariable final String code) {
        return toResponse(this.applicationService.getApplication(code));
    }

    @PostMapping(
            consumes = APPLICATION_JSON_VALUE,
            produces = APPLICATION_JSON_VALUE
    )
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponseApi createApplication(@RequestBody final CreateApplicationRequestApi request) {
        return toResponse(this.applicationService.createApplication(
                request.code(),
                request.name(),
                request.description()
        ));
    }
}
