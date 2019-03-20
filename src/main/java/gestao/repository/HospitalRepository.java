package gestao.repository;

import gestao.model.Hospital;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface HospitalRepository extends MongoRepository<Hospital, String> {
}