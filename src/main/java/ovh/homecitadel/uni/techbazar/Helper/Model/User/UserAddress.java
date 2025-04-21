package ovh.homecitadel.uni.techbazar.Helper.Model.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.util.Objects;

@Setter
@Getter
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
    @JsonProperty("address_name")
    private String addressName;

    @JsonProperty("default")
    private boolean defaultAddress;

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

    public UserAddress(String state, String country, String postalCode, String street, String civic, String addressName, boolean isDefault) {
        this.state = state;
        this.country = country;
        this.postalCode = postalCode;
        this.street = street;
        this.civic = civic;
        this.addressName = addressName;
        this.defaultAddress = isDefault;
    }

    @Override
    public int hashCode() {
        return Objects.hash(state, country, postalCode, street, civic);
    }

}
