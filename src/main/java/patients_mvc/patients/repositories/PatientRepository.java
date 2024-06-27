package patients_mvc.patients.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import patients_mvc.patients.entities.Patient;

public interface PatientRepository extends JpaRepository<Patient,Long> {
    Page<Patient> findByNomContains(String kw, Pageable pageable);
}
