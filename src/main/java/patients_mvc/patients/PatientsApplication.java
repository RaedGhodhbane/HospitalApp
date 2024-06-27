package patients_mvc.patients;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.JdbcUserDetailsManager;
import patients_mvc.patients.entities.Patient;
import patients_mvc.patients.repositories.PatientRepository;
import patients_mvc.patients.security.service.AccountService;

import java.util.Date;

@SpringBootApplication
public class PatientsApplication {

	public static void main(String[] args) {
		SpringApplication.run(PatientsApplication.class, args);
	}


	@Bean
	CommandLineRunner start(PatientRepository patientRepository) {
		return args -> {
			patientRepository.save(
					new Patient(null, "Hassan", new Date(), false, 122));
			patientRepository.save(
					new Patient(null, "Mohammed", new Date(), true, 321));
			patientRepository.save(
					new Patient(null, "Yasmine", new Date(), true, 165));
			patientRepository.save(
					new Patient(null, "Hanae", new Date(), false, 132));
			patientRepository.findAll().forEach(p -> {
				System.out.println(p.getNom());
			});
		};
	}

	//@Bean
	CommandLineRunner commandLineRunner(JdbcUserDetailsManager jdbcUserDetailsManager) {
		PasswordEncoder passwordEncoder=passwordEncoder();
		return args -> {

			UserDetails u1 = jdbcUserDetailsManager.loadUserByUsername("user11");

			if (u1 == null)
				jdbcUserDetailsManager.createUser(
					User.withUsername("user11").password(passwordEncoder.encode("1234")).authorities("USER").build()
			);
			UserDetails u2 = jdbcUserDetailsManager.loadUserByUsername("user22");

			if (u2 == null)
			jdbcUserDetailsManager.createUser(
					User.withUsername("user22").password(passwordEncoder.encode("1234")).authorities("USER").build()
			);

			UserDetails u3 = jdbcUserDetailsManager.loadUserByUsername("admin2");

			if (u3 == null)
			jdbcUserDetailsManager.createUser(
					User.withUsername("admin2").password(passwordEncoder.encode("1234")).authorities("USER", "ADMIN").build()
			);

		};
	}

	//@Bean
	CommandLineRunner commandLineRunnerUserDetails(AccountService accountService) {
		return args -> {
			accountService.addNewRole("USER");
			accountService.addNewRole("ADMIN");
			accountService.addNewUser("user1", "1234", "user1@gmail.com"
			, "1234");
			accountService.addNewUser("user2", "1234", "user2@gmail.com"
					, "1234");
			accountService.addNewUser("admin", "1234", "admin@gmail.com"
					, "1234");

			accountService.addRoleToUser("user1", "USER");
			accountService.addRoleToUser("user2", "USER");
			accountService.addRoleToUser("admin", "USER");
			accountService.addRoleToUser("admin", "ADMIN");


		};
	}

	@Bean
	PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
}
