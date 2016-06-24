package main.java.appdirect.interview.appdirectchallenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CreatorInfo {

	public String email;
	public String firstName;
	public String lastName;
	public String language;
	public String openId;
	public String uuid;

	@JsonCreator
	public CreatorInfo(@JsonProperty("uuid") String uuid,
			@JsonProperty("openId") String openId,
			@JsonProperty("email") String email,
			@JsonProperty("firstName") String firstName,
			@JsonProperty("lastName") String lastName,
			@JsonProperty("language") String language) {
		this.uuid = uuid;
		this.email = email;
		this.firstName = firstName;
		this.language = language;
		this.lastName = lastName;
		this.openId = openId;
	}

}