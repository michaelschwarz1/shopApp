package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import java.io.Serializable;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;


public class Artikel implements JsonMappable, Serializable {
	private static final long serialVersionUID = -2998318045100459809L;
	public Long id;
	public String beschreibung;
	public int version;
	public int aufLager;
	public String kategorie;
	public double preis;
	
	public Artikel(Long id, String beschreibung, String kategorie, int aufLager, Double preis) {
		super();
		this.id = id;
		this.beschreibung = beschreibung;
		this.kategorie = kategorie;
		this.aufLager = aufLager;
		this.preis = preis;
	}
	public Artikel(){
		super();
	}
	protected JsonObjectBuilder getJsonObjectBuilder() {
		return jsonBuilderFactory.createObjectBuilder()
				                 .add("pkArtikel", id)
			                     .add("version", version)
			                     .add("beschreibung", beschreibung)
			                     .add("preis", preis)
			                     .add("kategorie", kategorie)
			                     .add("aufLager", aufLager);
	}
	
	@Override
	public JsonObject toJsonObject() {
		return getJsonObjectBuilder().build();
	}

	public void fromJsonObject(JsonObject jsonObject) {
		id = Long.valueOf(jsonObject.getJsonNumber("pkArtikel").longValue());
	    version = jsonObject.getInt("version");
		beschreibung = jsonObject.getString("beschreibung");
		preis = jsonObject.getJsonNumber("preis").doubleValue();
		kategorie = jsonObject.getString("kategorie");
		aufLager = jsonObject.getInt("aufLager");
	}
	
	@Override
	public void updateVersion() {
		version++;
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
