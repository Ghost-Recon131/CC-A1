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
    private Long id; // id of the user creating new item listing
    private String listingTitle;
    private Double price;
    private String itemCondition;
    private String description;
}
