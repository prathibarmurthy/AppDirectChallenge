package main.java.appdirect.interview.appdirectchallenge.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class CompanyInfo {

    public String uuid;
    public String email;
    public String name;
    public String phoneNumber;
    public String website;

    @JsonCreator
    public CompanyInfo(@JsonProperty("uuid") String uuid,
                   @JsonProperty("email") String email,
                   @JsonProperty("name") String name,
                   @JsonProperty("phoneNumber") String phoneNumber,
                   @JsonProperty("website") String website) {
        this.uuid = uuid;
        this.email = email;
        this.name = name;
        this.phoneNumber = phoneNumber;
        this.website = website;
    }

}