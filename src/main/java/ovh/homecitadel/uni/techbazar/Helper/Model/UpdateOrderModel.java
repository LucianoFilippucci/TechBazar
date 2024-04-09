package ovh.homecitadel.uni.techbazar.Helper.Model;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Getter;
import lombok.Setter;
import ovh.homecitadel.uni.techbazar.Helper.OrderStatusEnum;

@Getter
@Setter
public class UpdateOrderModel {
    private String trackingCode;
    private String express;
    @JsonProperty("orderStatus")
    @Enumerated(EnumType.STRING)
    private OrderStatusEnum orderStatus;

    public void setOrderStatus(String orderStatus) {
        this.orderStatus = OrderStatusEnum.valueOf(orderStatus);
    }
}
