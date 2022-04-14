package rmit.cc.a1.ItemListing.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;
import rmit.cc.a1.utils.ItemCondition;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class NewItemListingRequest {
    private String ListingTitle;
    private Double price;
    private ItemCondition itemCondition;
    private String description;
}
