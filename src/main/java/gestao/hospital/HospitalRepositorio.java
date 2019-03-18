package gestao.hospital;

import org.springframework.data.mongodb.repository.MongoRepository;

public interface HospitalRepositorio extends MongoRepository<Hospital, String> {
}