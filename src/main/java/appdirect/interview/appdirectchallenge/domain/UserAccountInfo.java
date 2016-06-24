package main.java.appdirect.interview.appdirectchallenge.domain;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class UserAccountInfo {

    public Long id;
    public String openId;
    public String firstname;
    public String lastname;
    public String email;
    public Long subscriptionId;

    private UserAccountInfo() {
    }

    private UserAccountInfo(Builder builder) {
        this.id = builder.id;
        this.firstname = builder.firstname;
        this.lastname = builder.lastname;
        this.openId = builder.openId;
        this.email = builder.email;
        this.subscriptionId = builder.subscriptionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        UserAccountInfo user = (UserAccountInfo) o;

        if (id != null ? !id.equals(user.id) : user.id != null) return false;
        if (openId != null ? !openId.equals(user.openId) : user.openId != null) return false;
        if (firstname != null ? !firstname.equals(user.firstname) : user.firstname != null) return false;
        if (lastname != null ? !lastname.equals(user.lastname) : user.lastname != null) return false;
        if (email != null ? !email.equals(user.email) : user.email != null) return false;
        return subscriptionId != null ? subscriptionId.equals(user.subscriptionId) : user.subscriptionId == null;

    }

    @Override
    public int hashCode() {
        int result = id != null ? id.hashCode() : 0;
        result = 31 * result + (openId != null ? openId.hashCode() : 0);
        result = 31 * result + (firstname != null ? firstname.hashCode() : 0);
        result = 31 * result + (lastname != null ? lastname.hashCode() : 0);
        result = 31 * result + (email != null ? email.hashCode() : 0);
        result = 31 * result + (subscriptionId != null ? subscriptionId.hashCode() : 0);
        return result;
    }

    public static class Builder {

        private Long id;
        private String openId;
        private String firstname;
        private String lastname;
        private String email;
        private Long subscriptionId;

        public Builder id(Long id) {
            this.id = id;
            return this;
        }

        public Builder openId(String openId) {
            this.openId = openId;
            return this;
        }

        public Builder name(String firstname, String lastname) {
            this.firstname = firstname;
            this.lastname = lastname;
            return this;
        }

        public Builder email(String email) {
            this.email = email;
            return this;
        }

        public Builder subscriptionId(Long subscriptionId) {
            this.subscriptionId = subscriptionId;
            return this;

        }

        public UserAccountInfo build() {
            return new UserAccountInfo(this);
        }

    }

}