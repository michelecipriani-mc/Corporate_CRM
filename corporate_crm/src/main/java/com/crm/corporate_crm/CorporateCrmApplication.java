package com.crm.corporate_crm;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class CorporateCrmApplication {

	// ModelMapper semplifica il mapping tra oggetti con campi simili
	@Bean
	public ModelMapper instanceModelMapper() {
		ModelMapper mapper = new ModelMapper();
		return mapper;
	}

	public static void main(String[] args) {
		SpringApplication.run(CorporateCrmApplication.class, args);
	}

}
