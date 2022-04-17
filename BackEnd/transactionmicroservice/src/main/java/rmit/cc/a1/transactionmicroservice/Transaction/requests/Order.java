package rmit.cc.a1.transactionmicroservice.Transaction.requests;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Data
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class Order {
    private Long buyerID;
    private Long sellerID;
    private Long itemListingID;
    private Double price;
    private String currency;
}