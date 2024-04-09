package ovh.homecitadel.uni.techbazar.Helper.Model.Auction;

import lombok.*;

@Getter
@Setter
@EqualsAndHashCode
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class BidResponse {
    private boolean accepted; // if true -> auction open and no one won. if it's false, check winnername
    private String cause;
    private String error;

}
