package rmit.cc.a1.transactionmicroservice.Transaction.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "transaction")
public class Transactions {

    @Id
    @Column(name = "webapp_transactionid")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long TransactionID;

    @Column(name = "paypal_pay_id")
    private String paypalPayID;

    @Column(name = "paypal_Token")
    private String paypalToken;

    @Column(name = "buyerID")
    private Long buyerID;

    @Column(name = "SellerID")
    private Long sellerID;
    @Column(name = "TimeOfPurchase")
    private Date TimeOfPurchase;

    @Column(name = "update_At")
    private Date update_At;

    @Column(name = "itemListing_id")
    private Long itemListingID;

    // intent of transfer, required by PayPal API
    @Column(name = "intent")
    private String intent;

    @Column(name = "currency")
    private String currency;

    @Column(name = "price")
    private Double price;

    @Column(name = "Status")
    @Enumerated(EnumType.STRING)
    private Status status;

    @Column(name = "tracking_number")
    private String TrackingNumber;

    @PrePersist
    protected void onCreate() {
        this.TimeOfPurchase = new Date();
        this.intent = "item sale";
        this.status = Status.PENDING;
    }

    public Transactions(String paypalPayID, String paypalToken, Long buyerID, Long sellerID, Long itemListingID, Double price, String currency, String trackingNumber) {
        this.paypalPayID = paypalPayID;
        this.paypalToken = paypalToken;
        this.buyerID = buyerID;
        this.sellerID = sellerID;
        this.itemListingID = itemListingID;
        this.price = price;
        this.currency = currency;
        TrackingNumber = trackingNumber;
    }

}