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
public class Transaction {

    @Id
    @Column(name = "TransactionID")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long TransactionID;

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

    @Column(name = "currency")
    private String currency;

    @Column(name = "price")
    private Double price;

    @Column(name = "Status")
    private String status;

    @Column(name = "TrackingNumber")
    private String TrackingNumber;

    @PrePersist
    protected void onCreate() {
        this.TimeOfPurchase = new Date();
        this.status = "Processing";
    }

    public Transaction(Long buyerID, Long sellerID, Long itemListingID, Double price, String currency, String trackingNumber) {
        this.buyerID = buyerID;
        this.sellerID = sellerID;
        this.itemListingID = itemListingID;
        this.price = price;
        this.currency = currency;
        TrackingNumber = trackingNumber;
    }

}