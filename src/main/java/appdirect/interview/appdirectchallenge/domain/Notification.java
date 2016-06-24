package main.java.appdirect.interview.appdirectchallenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class Notification {

	public Type type;
	public MarketPlace marketplace;
	public String applicationUuid;
	public EventFlag flag;
	public CreatorInfo creator;
	public Payload payload;

	@JsonCreator
	public Notification(@JsonProperty("type") Type type,
			@JsonProperty("marketplace") MarketPlace marketplace,
			@JsonProperty("applicationUuid") String applicationUuid,
			@JsonProperty("flag") EventFlag flag,
			@JsonProperty("creator") CreatorInfo creator,
			@JsonProperty("payload") Payload payload) {
		this.type = type;
		this.marketplace = marketplace;
		this.applicationUuid = applicationUuid;
		this.flag = flag;
		this.creator = creator;
		this.payload = payload;
	}

	public enum Type {
		SUBSCRIPTION_ORDER, SUBSCRIPTION_CHANGE, SUBSCRIPTION_CANCEL, SUBSCRIPTION_NOTICE, USER_ASSIGNMENT, USER_UNASSIGNMENT, USER_UPDATED
	}

	public enum EventFlag {
		DEVELOPMENT, STATELESS
	}

}