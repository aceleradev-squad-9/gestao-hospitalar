package gestao.model;

import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class BaseEntity {

	private boolean deleted;

	public boolean isDeleted() {
		return deleted;
	}
}
