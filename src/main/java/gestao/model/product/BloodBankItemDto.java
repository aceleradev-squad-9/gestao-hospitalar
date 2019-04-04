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
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd/MM/yyyy HH:mm")
	private LocalDateTime dateDonation;

    @NotNull(message = "Informe o tipo sanguíneo")
    private BloodType bloodType;

    public Integer getAmount() {
		return amount;
	}

	public LocalDateTime getDateDonation() {
		return dateDonation;
	}

    public BloodType getBloodType() {
        return bloodType;
    }

    public void setAmount(Integer amount) {
		this.amount = amount;
	}

	public void setDateDonation(LocalDateTime dateDonation) {
		this.dateDonation = dateDonation;
	}

    public void setBloodType(BloodType bloodType) {
        this.bloodType = bloodType;
    }
}
