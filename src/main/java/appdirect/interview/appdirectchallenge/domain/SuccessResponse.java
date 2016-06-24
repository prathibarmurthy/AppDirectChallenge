package main.java.appdirect.interview.appdirectchallenge.domain;

public class SuccessResponse extends Response {

    public String accountIdentifier;

    public SuccessResponse() {
        this.success = true;
    }

    public SuccessResponse(String accountIdentifier) {
        this();
        this.accountIdentifier = accountIdentifier;
    }
}