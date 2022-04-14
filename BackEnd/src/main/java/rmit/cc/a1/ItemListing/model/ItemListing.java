package rmit.cc.a1.ItemListing.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.utils.ItemCondition;

import javax.persistence.*;

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

}
