package rmit.cc.a1.ItemListing.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.utils.ItemCondition;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "listings")
public class ItemListing {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    // Foreign key to match who made the listing
    @JoinColumn(nullable = false, name = "account_id")
    @ManyToOne
    private Account account;

    // Heading of the listing that user will search for ie "AMD CPU"
    @Column(name = "listing_title")
    private String ListingTitle;

    @Column(name = "price")
    private Double price;

    @Enumerated(EnumType.STRING)
    @Column(name = "itemCondition")
    private ItemCondition itemCondition;

    @Column(name = "item_description")
    private String description;

    // Listing not suspended by default
    @Column(name = "isListingSuspended")
    private Boolean ListingSuspended = false;

    @Column(name = "create_At")
    private Date create_At = new Date();

    @Column(name = "update_At")
    private Date update_At;

    @PrePersist
    protected void onCreate(){
        this.create_At = new Date();
    }

    @PreUpdate
    protected void onUpdate(){
        this.update_At = new Date();
    }

    public ItemListing(Account account, String listingTitle, Double price, ItemCondition itemCondition, String description, Boolean listingSuspended, Date create_At, Date update_At) {
        this.account = account;
        ListingTitle = listingTitle;
        this.price = price;
        this.itemCondition = itemCondition;
        this.description = description;
        ListingSuspended = listingSuspended;
        this.create_At = create_At;
        this.update_At = update_At;
    }

}
