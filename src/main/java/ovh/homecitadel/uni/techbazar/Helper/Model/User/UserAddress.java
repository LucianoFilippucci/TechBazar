package ovh.homecitadel.uni.techbazar.Helper.Model.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

public class UserAddress {
    @JsonProperty("state")
    private String state;
    @JsonProperty("country")
    private String country;
    @JsonProperty("postalCode")
    private String postalCode;
    @JsonProperty("street")
    private String street;
    @JsonProperty("civic")
    private String civic;

    @Override
    public String toString() {
        return "UserAddress{" +
                "state='" + state + '\'' +
                ", country='" + country + '\'' +
                ", postalCode='" + postalCode + '\'' +
                ", street='" + street + '\'' +
                ", civic='" + civic + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAddress that = (UserAddress) o;
        return Objects.equals(state, that.state) && Objects.equals(country, that.country) && Objects.equals(postalCode, that.postalCode) && Objects.equals(street, that.street) && Objects.equals(civic, that.civic);
    }

    public UserAddress() {
    }

    public UserAddress(String state, String country, String postalCode, String street, String civic) {
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.street = street;
        this.civic = civic;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, country, postalCode, street, civic);
    }

    public void setState(String state) {
        this.state = state;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public void setCivic(String civic) {
        this.civic = civic;
    }

    public String getState() {
        return state;
    }

    public String getCountry() {
        return country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getStreet() {
        return street;
    }

    public String getCivic() {
        return civic;
    }
}
