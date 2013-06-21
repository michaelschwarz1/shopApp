package de.shop.data;

import static de.shop.ShopApp.jsonBuilderFactory;

import java.io.Serializable;
import java.util.Date;

import javax.json.JsonObject;


public class Bestellung implements JsonMappable, Serializable {
	private static final long serialVersionUID = -3227854872557641281L;
	
	public Long id;
	public int version;
	public Date datum;

	public Bestellung() {
		super();
	}

	public Bestellung(long id, Date datum) {
		super();
		this.id = id;
		this.datum = datum;
	}

	@Override
	public JsonObject toJsonObject() {
		return jsonBuilderFactory.createObjectBuilder()
		                         .add("pkBestellung", id)
		                         .add("version", version)
		                         .build();
	}
	
	@Override
	public void fromJsonObject(JsonObject jsonObject) {
		id = Long.valueOf(jsonObject.getJsonNumber("pkBestellung").longValue());
		version = jsonObject.getInt("version");
	}
	
	@Override
	public void updateVersion() {
		version++;
	}

	@Override
	public String toString() {
		return "Bestellung [id=" + id +  "]";
	}
}
