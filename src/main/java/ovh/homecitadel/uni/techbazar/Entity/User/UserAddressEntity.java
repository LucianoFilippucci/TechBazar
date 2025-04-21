package ovh.homecitadel.uni.techbazar.Entity.User;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.type.NumericBooleanConverter;
import org.hibernate.type.StandardBooleanConverter;
import ovh.homecitadel.uni.techbazar.Helper.Model.User.UserAddress;

import java.util.Objects;


@Setter
@Getter
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


    @Basic
    @Column(name = "address_name")
    private String addressName;

    @Column(name = "is_default")
    private boolean isDefault;

    public UserAddressEntity() {}

    public UserAddressEntity(UserAddress adr, String userId) {
        this.country = adr.getCountry();
        this.civic = adr.getCivic();
        this.state = adr.getState();
        this.street = adr.getStreet();
        this.postalCode = adr.getPostalCode();
        this.userId = userId;
        this.addressName = adr.getAddressName();
        this.isDefault = adr.isDefaultAddress();
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

}
