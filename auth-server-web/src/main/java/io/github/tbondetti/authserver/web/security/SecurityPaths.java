package io.github.tbondetti.authserver.web.security;

import lombok.experimental.UtilityClass;

@UtilityClass
public class SecurityPaths {

    public static final String ACTUATOR_ALL = "/actuator/**";

    public static final String SWAGGER_UI_ALL = "/swagger-ui/**";

    public static final String SWAGGER_UI_HTML = "/swagger-ui.html";

    public static final String OPENAPI_DOCS_ALL = "/v3/api-docs/**";

    public static final String OPENAPI_DOCS_YAML = "/v3/api-docs.yaml";

    public static final String FAVICON = "/favicon.ico";

    public static final String ERROR = "/error";

    public static final String DEFAULT_UI = "/default-ui.css";
}
