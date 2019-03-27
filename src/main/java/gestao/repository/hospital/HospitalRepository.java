package gestao.repository.hospital;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import gestao.model.hospital.Hospital;

@Repository
public interface HospitalRepository extends CrudRepository<Hospital, Long> {

	List<Hospital> findAll();
	
	List<Hospital> findAllByIdNot(Long id);
}