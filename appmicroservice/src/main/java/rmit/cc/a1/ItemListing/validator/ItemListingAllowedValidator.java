package rmit.cc.a1.ItemListing.validator;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.Validator;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.ItemListing.model.ItemListing;

@Component
@AllArgsConstructor
public class ItemListingAllowedValidator implements Validator {

    @Override
    public boolean supports(Class<?> aClass) {
        return Account.class.equals(aClass);
    }

    @Override
    public void validate(Object object, Errors errors) {

        ItemListing itemListing = (ItemListing) object;

        if (itemListing.getListingSuspended()){
            errors.rejectValue("ItemListing", "isListingSuspended", "Listing has been suspended by admin, contact admin for more info");
        }

    }

}
