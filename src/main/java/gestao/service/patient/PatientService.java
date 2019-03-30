package gestao.service.patient;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestao.model.patient.Patient;
import gestao.repository.patient.PatientRepository;

/**
 * Classe responsável pela implementação dos serviços relacionados ao paciente.
 * 
 * @author silasalvares
 *
 */
@Service
public class PatientService {
	
	@Autowired
	private PatientRepository repository;
	
	public Patient create(Patient patient) {

		/*if (this.existsByCpf(person.getCpf())) {
			throw new PersonsWithSameCpfException();
		}*/

		return this.repository.save(patient);
	}

}
