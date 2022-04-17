package rmit.cc.a1.ItemListing.model;

import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@EqualsAndHashCode
@NoArgsConstructor
@Entity
@Table(name = "images")
public class ItemImages {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "image_id")
    private long id;

    // Foreign key to match the image to the listing id
    @ManyToOne
    @JoinColumn(nullable = false, name = "itemListing_id")
    private ItemListing itemListing;

    @Column(name = "image_name")
    private String imageName;

    // S3 link to the image, image not stored directly in database
    @Column(name = "image_link")
    private String imageLink;

    @Column(name = "tmp_listingID")
    private Integer tmpImageId;

    public ItemImages(ItemListing itemListing, String imageName, String imageLink, Integer tmpImageId) {
        this.itemListing = itemListing;
        this.imageName = imageName;
        this.imageLink = imageLink;
        this.tmpImageId = tmpImageId;
    }

}
