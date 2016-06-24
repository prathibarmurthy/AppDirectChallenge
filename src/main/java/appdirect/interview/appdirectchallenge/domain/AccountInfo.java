package main.java.appdirect.interview.appdirectchallenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AccountInfo {

    public String accountIdentifier;
    public String status;

    @JsonCreator
    public AccountInfo(@JsonProperty("accountIdentifier") String accountIdentifier,
                   @JsonProperty("status") String status) {
        this.accountIdentifier = accountIdentifier;
        this.status = status;
    }

}