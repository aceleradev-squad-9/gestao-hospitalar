package gestao.repository.hospital;

import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import gestao.model.hospital.Hospital;

@Repository
public interface HospitalRepository extends PagingAndSortingRepository<Hospital, Long> {

	List<Hospital> findAll();
	
	List<Hospital> findAllByIdNot(Long id);
}