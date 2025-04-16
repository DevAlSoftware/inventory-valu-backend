package com.company.inventory;

import com.company.inventory.user.services.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EnableJpaRepositories
@SpringBootApplication
public class InventoryValuApplication implements CommandLineRunner {

	@Autowired
	private IUserService usuarioService;

	public static void main(String[] args) {
		SpringApplication.run(InventoryValuApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {

	}
}
