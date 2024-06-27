package patients_mvc.patients.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import patients_mvc.patients.security.entities.AppUser;

public interface AppUserRepository extends JpaRepository<AppUser, String> {
    AppUser findByUsername(String username);
}
