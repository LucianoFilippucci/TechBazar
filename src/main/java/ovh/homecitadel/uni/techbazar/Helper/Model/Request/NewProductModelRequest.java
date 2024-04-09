package ovh.homecitadel.uni.techbazar.Helper.Model.Request;

import lombok.Getter;
import lombok.Setter;
import ovh.homecitadel.uni.techbazar.Helper.Model.Model;

import java.math.BigDecimal;
import java.util.Collection;

@Getter
@Setter
public class NewProductModelRequest {
    protected Long productId;
    protected Collection<Model> models;
}

