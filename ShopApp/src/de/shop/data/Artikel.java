package de.shop.data;

import java.io.Serializable;


public class Artikel implements Serializable {
	private static final long serialVersionUID = -2998318045100459809L;
	public Long id;
	public String beschreibung;
	
	public Artikel(Long id, String beschreibung) {
		super();
		this.id = id;
		this.beschreibung = beschreibung;
	}
	
	@Override
	public String toString() {
		return "Artikel [id=" + id + ", beschreibung=" + beschreibung + "]";
	}

	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result
				+ ((beschreibung == null) ? 0 : beschreibung.hashCode());
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Artikel other = (Artikel) obj;
		if (beschreibung == null) {
			if (other.beschreibung != null)
				return false;
		} else if (!beschreibung.equals(other.beschreibung))
			return false;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
}
