package rmit.cc.a1.ItemListing.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.ItemListing.model.ItemListing;

@Component
@AllArgsConstructor
public class ItemListingValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Account.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {


        ItemListing itemListing = (ItemListing) object;

        if (itemListing.getListingTitle() == null){
            errors.rejectValue("ItemListing", "ListingTitle", "Listing Title cannot be empty");
        }

        if (itemListing.getPrice() == null){
            errors.rejectValue("ItemListing", "Price", "Price of item cannot be empty");
        }

        if (itemListing.getItemCondition() == null){
            errors.rejectValue("ItemListing", "ItemCondition", "Item Condition cannot be empty");
        }

        if (itemListing.getDescription() == null){
            errors.rejectValue("ItemListing", "Description", "Description cannot be empty");
        }

    }

}
