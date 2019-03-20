package gestao.repository.hospital;

import org.springframework.data.mongodb.repository.MongoRepository;

import gestao.model.hospital.Hospital;

public interface HospitalRepository extends MongoRepository<Hospital, String> {
}