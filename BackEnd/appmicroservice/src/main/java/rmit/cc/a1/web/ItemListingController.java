package rmit.cc.a1.web;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rmit.cc.a1.AWSConfig.s3.service.S3Service;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.Account.services.MapValidationErrorService;
import rmit.cc.a1.ItemListing.model.ItemListing;
import rmit.cc.a1.ItemListing.repository.ItemListingRepository;
import rmit.cc.a1.ItemListing.requests.NewItemListingRequest;
import rmit.cc.a1.ItemListing.services.ItemImagesService;
import rmit.cc.a1.ItemListing.services.ItemListingService;
import rmit.cc.a1.ItemListing.validator.ItemListingValidator;

import java.util.List;
import java.util.Random;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/itemListings")
@AllArgsConstructor
public class ItemListingController {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private ItemListingValidator itemListingValidator;
    private MapValidationErrorService mapValidationErrorService;
    private ItemListingRepository itemListingRepository;
    private ItemListingService itemListingService;
    private AccountRepository accountRepository;
    private ItemImagesService itemImagesService;
    private S3Service s3Service;

    // Get all listings
    @GetMapping(path = "/viewAllListings")
    public List<ItemListing> viewAllListings(){
        return itemListingRepository.findAll();
    }

    @GetMapping(path = "/viewListingByID")
    public ItemListing viewListingByID(@RequestParam("id")Long id){
        Long varLong = Long.parseLong(String.valueOf(id));
        return itemListingRepository.getById(varLong);
    }

    // Checks item listing
    @PostMapping(path = "/newItemListing")
    public Long newItemListing(@RequestBody NewItemListingRequest listingRequest, BindingResult result){
        Integer tmpListingID = new Random().nextInt(10000);
        ItemListing newItemListing = itemListingService.newItemListing(listingRequest, tmpListingID);

        if(newItemListing != null){
            itemListingValidator.validate(newItemListing, result);
            ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
            if(errorMap != null){
                throw new org.springframework.security.access.AccessDeniedException(errorMap.toString());
            }
        }else{
            // Returns null if RequestBody is empty
            return null;
        }

        itemListingService.createS3BucketForUser(listingRequest.getId());

        return itemListingService.getNewListingID(tmpListingID);
    }

    // Add images to item listing
    @PostMapping(path = "/addImageToListing/{id}")
    public String addImageToListing(@PathVariable(value = "id") Long id,@RequestParam(value = "userId") Long userId,
                                    @RequestParam(value = "file") MultipartFile multipartFile, @RequestParam(value = "filename") String filename) {

        Integer tmpImageId = new Random().nextInt(10000);

        return itemImagesService.addImageToListing(id, userId, multipartFile, filename, tmpImageId);
    }

    // Returns image link for a particular item
    @GetMapping(path = "/getListingImageLinks/{id}")
    public List<String> getListingImageLinks(@PathVariable(value = "id") Long id){
        return itemImagesService.getListingImageLinks(id);
    }

    // Modify item listing details
    @PutMapping(path = "/modifyItemListing")
    public ResponseEntity<?>modifyItemListing(BindingResult result, @RequestBody NewItemListingRequest listingRequest){
        Account currentUser = accountRepository.getById(listingRequest.getId());

        if(currentUser.getId() == itemListingRepository.getById(listingRequest.getId()).getAccountId()){
            ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
            if(errorMap != null) return errorMap;

            itemListingService.updateItemListingDetails(listingRequest.getId(), listingRequest);
        }else{ // when modifying listing that does not belong to the current user
            throw new org.springframework.security.access.AccessDeniedException("403 returned, can only modify your own listings");
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    //TODO: BELOW ARE TEMP API ENDPOINTS, DELETE ONCE COMPLETED TESTING
    @PostMapping(path = "/deleteS3Bucket/{id}")
    public ResponseEntity<?> deleteS3Bucket(@PathVariable(value = "id") Long id){
        Account currentUser = accountRepository.getById(id);

        s3Service.deleteS3Bucket(currentUser.getUserRole().toString(), currentUser.getId(), currentUser.getUuid());
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    //TODO: DELETE TEST METHODS ABOVE

}
