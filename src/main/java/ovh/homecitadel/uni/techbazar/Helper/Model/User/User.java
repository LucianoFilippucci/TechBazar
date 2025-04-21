package ovh.homecitadel.uni.techbazar.Helper.Model.User;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class User {
    private String firstName;
    private String lastName;
    private String email;
    private String password;
    private String username;
    @JsonProperty(value = "pIva")
    private String pIva;
    private String cartId;
    private String phoneNumber;
}
