package com.salesianos.dam.gestionpeliculas;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(info =
@Info(title = "API de Gestión de Películas",
		description = "API REST para gestionar películas, directores y el reparto de actores",
		version = "1.0",
		contact = @Contact(name = "Raúl Ramírez Domínguez")
)
)
@SpringBootApplication
public class Application {

	public static void main(String[] args) {
		SpringApplication.run(Application.class, args);
	}

}