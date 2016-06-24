package main.java.appdirect.interview.appdirectchallenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Payload {

	public CreatorInfo user;
	public AccountInfo account;
	public CompanyInfo company;
	public SubscriptionOrder order;

	@JsonCreator
	public Payload(@JsonProperty("user") CreatorInfo user,
			@JsonProperty("account") AccountInfo account,
			@JsonProperty("company") CompanyInfo company,
			@JsonProperty("order") SubscriptionOrder order) {
		this.account = account;
		this.company = company;
		this.user = user;
		this.order = order;
	}
}