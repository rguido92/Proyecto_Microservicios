package com.microservicios.apigateaway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class ApigateawayApplication {

	public static void main(String[] args) {
		SpringApplication.run(ApigateawayApplication.class, args);
	}
	@Bean
	public RouteLocator customRouteLocator(RouteLocatorBuilder builder) {
		return builder.routes()
				// Permitir el acceso a la ruta de la api usuarios
				.route("usuarios", r -> r.path("/usuarios/**")
						.uri("lb://usuarios"))
				// Permitir el acceso a la ruta de la api reservas
				.route("reservas", r -> r.path("/reservas/**")
						.uri("lb://reservas"))
				// Permitir el acceso al uso de la API comentarios
				.route("comentarios", r -> r.path("/comentarios/**")
						.filters(f -> f.rewritePath("/comentarios/(?<segment>.*)", "/${segment}"))
						.uri("lb://comentarios"))
				.build();
	}
}
