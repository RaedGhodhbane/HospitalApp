package patients_mvc.patients.security.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import patients_mvc.patients.security.entities.AppRole;

public interface AppRoleRepository extends JpaRepository<AppRole,String> {

}
