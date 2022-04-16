package rmit.cc.a1.web;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import rmit.cc.a1.AWSConfig.s3.service.S3Service;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.Account.services.MapValidationErrorService;
import rmit.cc.a1.ItemListing.model.ItemImages;
import rmit.cc.a1.ItemListing.model.ItemListing;
import rmit.cc.a1.ItemListing.repository.ItemImagesRepository;
import rmit.cc.a1.ItemListing.repository.ItemListingRepository;
import rmit.cc.a1.ItemListing.requests.NewItemListingRequest;
import rmit.cc.a1.ItemListing.services.ItemImagesService;
import rmit.cc.a1.ItemListing.services.ItemListingService;
import rmit.cc.a1.ItemListing.util.ItemListingCreateSucessReponse;
import rmit.cc.a1.ItemListing.validator.ItemListingValidator;

import java.util.List;
import java.util.Random;
import java.util.UUID;

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
    private ItemImagesRepository itemImagesRepository;
    private ItemImagesService itemImagesService;
    private S3Service s3Service;

    // Get all listings
    @GetMapping(path = "/viewAllListings")
    public List<ItemListing> viewAllListings(){
        return itemListingRepository.findAll();
    }

    // Checks item listing
    @PostMapping(path = "/newItemListing")
    public Long newItemListing(@RequestBody NewItemListingRequest listingRequest, BindingResult result){

        Integer tmpListingID = new Random().nextInt(10000);

        try{
            ItemListing newItemListing = new ItemListing(
                    listingRequest.getId(),
                    listingRequest.getListingTitle(),
                    listingRequest.getPrice(),
                    listingRequest.getItemCondition(),
                    listingRequest.getDescription(),
                    tmpListingID
            );

            itemListingValidator.validate(newItemListing, result);
            ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
            if(errorMap != null){
                throw new org.springframework.security.access.AccessDeniedException(errorMap.toString());
            }

            itemListingRepository.save(newItemListing);
        } catch (Exception e) {
            logger.error("Error creating new listing" + e);
            System.err.println("Error creating new listing" + e);
        }

        // Create bucket for user if not already exist
        try{
            Account currentUser = accountRepository.getById(listingRequest.getId());
            s3Service.createS3Bucket(currentUser.getUserRole().toString(), currentUser.getId(), currentUser.getUuid());
        } catch (Exception e) {
            logger.error("Error creating new S3 Bucket \n" + e);
            System.err.println("Error creating new S3 Bucket \n" + e);
        }

        return itemListingService.getNewListingID(tmpListingID);
    }

    // Add images to item listing
    @PostMapping(path = "/addImageToListing/{id}")
    public ResponseEntity<?>addImageToListing(@PathVariable(value = "id") Long id) {
        ItemListing toAddImage = itemListingRepository.getById(id);

        // TODO: Adds images to item listing via S3
        ItemImages newImage = new ItemImages(
                toAddImage,
                "NAME",
                "LINK"
        );

        itemImagesRepository.save(newImage);

        return new ResponseEntity<>(HttpStatus.CREATED);
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
