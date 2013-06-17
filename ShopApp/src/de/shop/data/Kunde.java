package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import java.io.Serializable;

import javax.json.JsonObject;
import javax.json.JsonObjectBuilder;

public class Kunde implements JsonMappable, Serializable {
	private static final long serialVersionUID = -7505776004556360014L;


	public Long id;
	public int version;
	public String nachname;
	public String vorname;
	public String email;
	public Adresse adresse;
	public String bestellungenUri;

	
	protected JsonObjectBuilder getJsonObjectBuilder() {
		return jsonBuilderFactory.createObjectBuilder()
				                 .add("pkKunde", id)
			                     .add("version", version)
			                     .add("nachname", nachname)
			                     .add("vorname", vorname)
			                     .add("email", email)
			                     .add("adresse", adresse.getJsonBuilderFactory())
			                     .add("bestellungenUri", bestellungenUri);
	}
	
	@Override
	public JsonObject toJsonObject() {
		return getJsonObjectBuilder().build();
	}

	public void fromJsonObject(JsonObject jsonObject) {
		id = Long.valueOf(jsonObject.getJsonNumber("pkKunde").longValue());
	    version = jsonObject.getInt("version");
		nachname = jsonObject.getString("nachname");
		vorname = jsonObject.getString("vorname");
		email = jsonObject.getString("email");
		adresse = new Adresse();
		adresse.fromJsonObject(jsonObject.getJsonObject("adresse"));
		bestellungenUri = jsonObject.getString("bestellungenUri");
	}
	

	@Override
	public void updateVersion() {
		version++;
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((email == null) ? 0 : email.hashCode());
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
		Kunde other = (Kunde) obj;
		if (email == null) {
			if (other.email != null)
				return false;
		} else if (!email.equals(other.email))
			return false;
		return true;
	}

	@Override
	public String toString() {
		return "AbstractKunde [id=" + id + ", nachname=" + nachname + ", vorname="
				+ vorname
				+ ", email=" + email + ", adresse=" + adresse
				+ ", bestellungenUri=" + bestellungenUri + "]";
	}
}
