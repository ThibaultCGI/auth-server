package io.github.tbondetti.authserver.infrastructure.web.controller;

import io.github.tbondetti.authserver.infrastructure.service.ApplicationService;
import io.github.tbondetti.authserver.infrastructure.web.dto.CreateApplicationRequest;
import io.github.tbondetti.authserver.infrastructure.web.response.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import static io.github.tbondetti.authserver.infrastructure.web.mapper.ApplicationWebMapper.toResponse;

@RestController
@RequestMapping("/api/applications")
@RequiredArgsConstructor
public class ApplicationController {

    private final ApplicationService applicationService;

    @GetMapping("/{code}")
    public ApplicationResponse getApplication(@PathVariable final String code) {
        return toResponse(this.applicationService.getApplication(code));
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public ApplicationResponse createApplication(@RequestBody final CreateApplicationRequest request) {
        return toResponse(this.applicationService.createApplication(
                request.code(),
                request.name(),
                request.description()
        ));
    }
}
