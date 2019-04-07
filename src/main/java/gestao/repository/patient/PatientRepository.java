package gestao.repository.patient;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gestao.model.hospital.Hospital;
import gestao.model.patient.Patient;
import gestao.model.person.Person;

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
	
	Long countByHospitalAndTimeCheckOutIsNull(Hospital hospital);

	List<Patient> findAllByHospitalAndTimeCheckOutIsNull(Hospital hospital);
	
	Optional<Patient> findByHospitalAndPersonAndTimeCheckOutIsNull(Hospital hospital, Person person);

	Boolean existsByHospitalAndPersonAndTimeCheckOutIsNull(Hospital hospital, Person person);

}
