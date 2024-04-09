package ovh.homecitadel.uni.techbazar.Helper.Model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

@Getter
@Setter
public class DailyOfferRequest {
    private Long dailyId;
    private int discount;
    private Long modelId;
    private Long productId;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate date;
}
