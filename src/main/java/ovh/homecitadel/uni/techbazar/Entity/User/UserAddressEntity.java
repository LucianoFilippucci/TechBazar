package ovh.homecitadel.uni.techbazar.Entity.User;

import jakarta.persistence.*;
import lombok.*;
import ovh.homecitadel.uni.techbazar.Helper.Model.User.UserAddress;

import java.util.Objects;


@Entity
@Table(name = "user_address", schema = "techbazar")
public class UserAddressEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "address_id", nullable = false)
    private Long addressId;

    @Basic
    @Column(name = "user_id", nullable = false)
    private String userId;

    @Basic
    @Column(name = "state", nullable = false)
    private String state;

    @Basic
    @Column(name = "country", nullable = false)
    private String country;

    @Basic
    @Column(name = "postal_code", nullable = false)
    private String postalCode;

    @Basic
    @Column(name = "street", nullable = false)
    private String street;

    @Basic
    @Column(name = "civic")
    private String civic;

    @Version
    @Column(name = "version")
    private Long version;

    public UserAddressEntity() {}

    public UserAddressEntity(UserAddress adr, String userId) {
        this.country = adr.getCountry();
        this.civic = adr.getCivic();
        this.state = adr.getState();
        this.street = adr.getStreet();
        this.postalCode = adr.getPostalCode();
        this.userId = userId;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        UserAddressEntity that = (UserAddressEntity) o;
        return Objects.equals(addressId, that.addressId) && Objects.equals(userId, that.userId) && Objects.equals(state, that.state) && Objects.equals(country, that.country) && Objects.equals(postalCode, that.postalCode) && Objects.equals(street, that.street) && Objects.equals(civic, that.civic) && Objects.equals(version, that.version);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(this.street).append(",").append(this.civic).append(",").append(this.state).append(",").append(this.postalCode).append(",").append(this.country);
        return sb.toString();
    }

    @Override
    public int hashCode() {
        return Objects.hash(addressId, userId, state, country, postalCode, street, civic, version);
    }

    public Long getAddressId() {
        return addressId;
    }

    public void setAddressId(Long addressId) {
        this.addressId = addressId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public void setPostalCode(String postalCode) {
        this.postalCode = postalCode;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getCivic() {
        return civic;
    }

    public void setCivic(String civic) {
        this.civic = civic;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }
}
