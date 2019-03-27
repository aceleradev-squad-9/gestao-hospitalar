package gestao.model;

import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonIgnore;

@MappedSuperclass
public abstract class BaseEntity {

	@JsonIgnore
	private final boolean deleted;

	protected BaseEntity() {
		this.deleted = false;
	}

	public boolean isDeleted() {
		return deleted;
	}
}
