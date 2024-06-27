package patients_mvc.patients.web;

import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import patients_mvc.patients.entities.Patient;
import patients_mvc.patients.repositories.PatientRepository;

import java.util.List;
import java.util.Optional;

@Controller
@AllArgsConstructor
public class PatientController {
    private PatientRepository patientRepository; // Pour accéder à la base de données

    @GetMapping(path = "/user/index")
    public String patients(Model model,
                           @RequestParam(name = "page", defaultValue = "0") int page,
                           @RequestParam(name = "size", defaultValue = "5") int size,
                           @RequestParam(name = "keyword", defaultValue = "") String keyword) {
        Page<Patient> pagePatients = patientRepository.findByNomContains(keyword,PageRequest.of(page,size));
        model.addAttribute("listPatients", pagePatients.getContent());
        model.addAttribute("pages", new int[pagePatients.getTotalPages()]);
        model.addAttribute("currentPage", page);
        model.addAttribute("keyword", keyword);
        return "patients";
    }

    @GetMapping(path = "/admin/delete")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String delete(Long id, String keyword, int page) {
        patientRepository.deleteById(id);
        return "redirect:/user/index?page="+page+"&keyword="+keyword;
    }

    @GetMapping(path = "/patients")
    @ResponseBody
    public List<Patient> listPatients() {
        return patientRepository.findAll();
    }

    @GetMapping(path = "/admin/formPatients")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String formPatient(Model model) {
        model.addAttribute("patient", new Patient());
        return "formPatients";
    }

    @PostMapping(path = "/admin/save")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String save(Model model, @Valid Patient patient, BindingResult bindingResult,
                       @RequestParam(defaultValue = "0") int page,
                       @RequestParam(defaultValue = "") String keyword) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("page", page);
            model.addAttribute("keyword", keyword);
            return "formPatients";
        }

        if (patient.getId() != null) {
            // Vérifie si le patient existe déjà dans la base de données
            Optional<Patient> existingPatientOpt = patientRepository.findById(patient.getId());
            if (existingPatientOpt.isPresent()) {
                Patient existingPatient = existingPatientOpt.get();
                // Met à jour les champs du patient existant
                existingPatient.setNom(patient.getNom());
                existingPatient.setDateNaissance(patient.getDateNaissance());
                existingPatient.setMalade(patient.isMalade());
                existingPatient.setScore(patient.getScore());
                patientRepository.save(existingPatient); // Met à jour le patient existant
            } else {
                // Si le patient n'existe pas (ce cas ne devrait normalement pas arriver), sauvegarde un nouveau patient
                patientRepository.save(patient);
            }
        } else {
            // Sauvegarde un nouveau patient
            patientRepository.save(patient);
        }

        return "redirect:/user/index?page=" + page + "&keyword=" + keyword;
    }


    @GetMapping(path = "/admin/editPatient")
    @PreAuthorize("hasAuthority('ADMIN')")
    public String editPatient(Model model, Long id, String keyword, int page) {
        Patient patient = patientRepository.findById(id).orElse(null);
        if (patient == null) throw new RuntimeException("Patient introuvable");
        model.addAttribute("patient", patient);
        model.addAttribute("page", page);
        model.addAttribute("keyword", keyword);
        return "editPatient";
    }

    @GetMapping(path = "/")
    public String home() {
        return "redirect:/user/index";
    }

    @GetMapping(path = "/logout")
    public String logout() {
        return "redirect:/login";
    }

}
