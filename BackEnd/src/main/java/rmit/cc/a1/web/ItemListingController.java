package rmit.cc.a1.web;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.Account.services.MapValidationErrorService;
import rmit.cc.a1.ItemListing.model.ItemListing;
import rmit.cc.a1.ItemListing.repository.ItemListingRepository;
import rmit.cc.a1.ItemListing.requests.NewItemListingRequest;
import rmit.cc.a1.ItemListing.services.ItemListingService;
import rmit.cc.a1.ItemListing.util.ItemListingCreateSucessReponse;
import rmit.cc.a1.ItemListing.validator.ItemListingValidator;
import rmit.cc.a1.utils.GetUserByJWTUtil;

import javax.servlet.http.HttpServletRequest;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/itemListings")
@AllArgsConstructor
public class ItemListingController {

    private ItemListingValidator itemListingValidator;
    private MapValidationErrorService mapValidationErrorService;
    private GetUserByJWTUtil getUserByJWTUtil;
    private ItemListingRepository itemListingRepository;
    private ItemListingService itemListingService;
    private AccountRepository accountRepository;

    // Checks item listing
    @PostMapping(path = "/newItemListing")
    public ResponseEntity<?>newItemListing(@RequestBody NewItemListingRequest listingRequest, BindingResult result){

        Account currentUser = accountRepository.getById(listingRequest.getId());

        ItemListing newItemListing = new ItemListing(
                listingRequest.getId(),
                listingRequest.getListingTitle(),
                listingRequest.getPrice(),
                listingRequest.getItemCondition(),
                listingRequest.getDescription()
        );

        itemListingValidator.validate(newItemListing, result);
        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        itemListingRepository.save(newItemListing);

        Long newListingId =  itemListingService.getNewListingID(currentUser.getId(), listingRequest);

        return ResponseEntity.ok(new ItemListingCreateSucessReponse(true, newListingId));
    }

    // TODO: Adds images to item listing
    @PostMapping(path = "/addImageToListing/{id}")
    public ResponseEntity<?>addImageToListing(@PathVariable(value = "id") Long id, HttpServletRequest request, BindingResult result) {

        // TODO: Adds images to item listing

        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    // Modify item listing details
    @PutMapping(path = "/modifyItemListing/{id}")
    public ResponseEntity<?>modifyItemListing(@PathVariable(value = "id") Long id, HttpServletRequest request, BindingResult result, @RequestBody NewItemListingRequest listingRequest){
        Account currentUser = getUserByJWTUtil.getUserIdByJWT(request);

        if(currentUser.getId() == itemListingRepository.getById(id).getAccountId()){
            ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
            if(errorMap != null) return errorMap;

            itemListingService.updateItemListingDetails(id, listingRequest);
        }else{ // when modifying listing that does not belong to the current user
            throw new org.springframework.security.access.AccessDeniedException("403 returned, can only modify your own listings");
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
