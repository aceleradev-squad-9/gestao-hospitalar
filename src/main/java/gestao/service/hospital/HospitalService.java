package gestao.service.hospital;

import java.time.LocalDateTime;
import java.util.List;

import gestao.model.bloodbank.BloodBank;
import gestao.model.product.BloodBankItem;
import gestao.model.product.BloodType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import gestao.exception.hospital.HospitalNotFoundException;
import gestao.exception.hospital.NearestHospitalNotFoundException;
import gestao.model.address.Address;
import gestao.model.hospital.Hospital;
import gestao.model.hospital.HospitalDto;
import gestao.model.product.Product;
import gestao.model.product.ProductItem;
import gestao.repository.hospital.HospitalRepository;

@Service
public class HospitalService {

	@Autowired
	private HospitalRepository hospitalRepository;

	@Autowired
	private HospitalGeoService hospitalGeoService;

	public Hospital createHospital(HospitalDto hospitalDto) {
		Hospital newHospital = Hospital.createFromDto(hospitalDto);
		return this.save(newHospital);
	}

	public Hospital save(Hospital hospital) {
		return this.hospitalRepository.save(hospital);
	}

	public Hospital findById(Long id) {
		return hospitalRepository.findById(id).orElseThrow(HospitalNotFoundException::new);
	}

	public void verifyIfExistsById(Long id) {
		this.findById(id);
	}

	public List<Hospital> findAll() {
		return hospitalRepository.findAll();
	}

	public void delete(Long id) {
		hospitalRepository.delete(this.findById(id));
	}

	public Hospital update(Long hospitalId, HospitalDto hospitalDto) {
		Hospital hospital = this.findById(hospitalId);
		hospital.updateFromDto(hospitalDto);
		return hospitalRepository.save(hospital);
	}

	public ProductItem addProductInStock(Long hospitalId, Product product, Integer amount) {

		Hospital hospital = this.findById(hospitalId);
		
		ProductItem productItem = hospital.addProductInStock(product, amount);

		this.save(hospital);

		return productItem;
	}

	public BloodBankItem addBloodBankInStock(Long hospitalId, Product product, Integer amount, LocalDateTime dateDonation, BloodType bloodType) {

		Hospital hospital = this.findById(hospitalId);

		BloodBankItem bloodBankItem = hospital.addBloodBankInStock(product, amount, dateDonation, bloodType);

		this.save(hospital);

		return bloodBankItem;
	}
	
	public ProductItem findProductInStock(Long hospitalId, Product product) {
		Hospital hospital = this.findById(hospitalId);
		return hospital.findProductInStock(product);
	}

	public BloodBankItem findBloodBankInStock(Long hospitalId, Product product) {
		Hospital hospital = this.findById(hospitalId);
		return hospital.findBloodBankInStock(product);
	}

	public BloodBankItem findBloodBankInStockByType(Long hospitalId, BloodType bloodType) {
//		Hospital hospital = this.findById(hospitalId);

		return null;
	}

	public ProductItem orderProductFromNearestHospitals(Long hospitalId, Product product, Integer amount) {
		Hospital originHospital = this.findById(hospitalId);

		List<Hospital> nearestHospitals = this.findNearestHospitals(originHospital);

		Hospital nearestHospital = nearestHospitals.stream().filter((hospital) -> hospital.reduceStock(product, amount))
				.findFirst().orElseThrow(() -> new NearestHospitalNotFoundException());

		ProductItem productItem = originHospital.addProductInStock(product, amount);

		this.save(originHospital);
		this.save(nearestHospital);

		return productItem;
	}

	public List<Hospital> findNearestHospitals(Hospital hospital) {

		List<Hospital> hospitals = this.hospitalRepository.findAllByIdNot(hospital.getId());

		return this.hospitalGeoService.findNearestHospitals(hospitals, hospital.getAddress());
	}

	public List<Hospital> findNearestHospitals(Address origin) {

		return this.hospitalGeoService.findNearestHospitals(this.findAll(), origin);
	}
}
