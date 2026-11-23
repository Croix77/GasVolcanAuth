package cl.duoc.gasvolcan.auth.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(info = @Info(
		title = "Gas El Volcan Auth API",
		version = "1.0",
		description = "Autenticacion y emision de JWT para Gas El Volcan"))
public class OpenApiConfig {
}
