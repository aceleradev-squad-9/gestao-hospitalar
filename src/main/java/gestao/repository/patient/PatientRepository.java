package gestao.repository.patient;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
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
public interface PatientRepository extends PagingAndSortingRepository<Patient, Long> {
	
	Long countByHospitalAndTimeCheckOutIsNull(Hospital hospital);

	Page<Patient> findAllByHospitalAndTimeCheckOutIsNull(Pageable pageable, Hospital hospital);
	
	Optional<Patient> findByHospitalAndPersonAndTimeCheckOutIsNull(Hospital hospital, Person person);

	Boolean existsByHospitalAndPersonAndTimeCheckOutIsNull(Hospital hospital, Person person);

}
