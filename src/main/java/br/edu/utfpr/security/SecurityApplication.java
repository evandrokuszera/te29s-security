package br.edu.utfpr.security;

import br.edu.utfpr.security.model.Role;
import br.edu.utfpr.security.repositories.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.support.BeanDefinitionDsl;

@SpringBootApplication
public class SecurityApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(SecurityApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		// Este código executa sempre que sua aplicação inicia
		// Adiciona as Roles ADMIN e BASIC senão existirem DB
		if (roleRepository.findAll().size() < 2){
			var adminRole = new Role();
			var basicRole = new Role();
			adminRole.setName("ADMIN");
			basicRole.setName("BASIC");
			roleRepository.save(adminRole);
			roleRepository.save(basicRole);
			System.out.println("Roles ADMIN e BASIC inseridas na base de dados.");
		}

	}

	@Autowired
	private RoleRepository roleRepository;
}
