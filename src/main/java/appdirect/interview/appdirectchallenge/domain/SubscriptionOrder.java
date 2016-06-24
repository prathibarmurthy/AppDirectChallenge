package main.java.appdirect.interview.appdirectchallenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class SubscriptionOrder {

    public String editionCode;
    public String pricingDuration;

    @JsonCreator
    public SubscriptionOrder(@JsonProperty("editionCode") String editionCode,
                 @JsonProperty("pricingDuration") String pricingDuration) {
        this.editionCode = editionCode;
        this.pricingDuration = pricingDuration;
    }

}