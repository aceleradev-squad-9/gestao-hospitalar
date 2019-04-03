package gestao.model.product;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateTimeDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateTimeSerializer;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

public class BloodBankItemDto {

	@NotNull(message = "Informe a quantidade do produto")
	@Min(value = 1, message="A quantidade do produto deve ser maior que 0")
	private Integer amount;


	@JsonSerialize(using = LocalDateTimeSerializer.class)
	@JsonDeserialize(using = LocalDateTimeDeserializer.class)
	private LocalDateTime dateDonation;

	public Integer getAmount() {
		return amount;
	}

	public LocalDateTime getDateDonation() {
		return dateDonation;
	}

	public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setDateDonation(LocalDateTime dateDonation) {
		this.dateDonation = dateDonation;
	}
}
