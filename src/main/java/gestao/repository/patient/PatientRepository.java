package gestao.repository.patient;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gestao.model.hospital.Hospital;
import gestao.model.patient.Patient;

/**
 * Interface responsável pelo repositório de dados de um paciente.
 * 
 * @author silasalvares
 *
 */
@Repository
public interface PatientRepository extends CrudRepository<Patient, Long> {

	List<Patient> findAll();
	
	List<Patient> findAllByIdNot(Long id);
}
