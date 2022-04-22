package rmit.cc.a1.ItemListing.requests;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@AllArgsConstructor
@EqualsAndHashCode
@ToString
public class ModifyItemListingRequest {
    private String listingTitle;
    private Double price;
    private String description;
}
