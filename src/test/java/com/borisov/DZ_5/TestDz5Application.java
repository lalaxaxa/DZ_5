package com.borisov.DZ_5;

import org.springframework.boot.SpringApplication;

public class TestDz5Application {

	public static void main(String[] args) {
		SpringApplication.from(Dz5Application::main).with(TestcontainersConfiguration.class).run(args);
	}

}
