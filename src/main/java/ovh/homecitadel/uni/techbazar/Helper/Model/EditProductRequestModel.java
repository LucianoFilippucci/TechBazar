package ovh.homecitadel.uni.techbazar.Helper.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class EditProductRequestModel {
    @JsonProperty("property")
    private String property;
    @JsonProperty("value")
    private String value;
    @JsonProperty("productId")
    private Long productId;
}
