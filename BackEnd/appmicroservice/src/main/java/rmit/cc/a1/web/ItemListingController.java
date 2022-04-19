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
    @PostMapping(path = "/newItemListing/{id}")
    public Long newItemListing(@PathVariable(value = "id") Long userID, @RequestBody NewItemListingRequest listingRequest, BindingResult result){
        //TODO: DEBUG CODE
        System.err.println("ID from frontend " + userID);
        System.err.println("getListingTitle " + listingRequest.getListingTitle());
        System.err.println("price " + listingRequest.getPrice());
        System.err.println("itemCondition " + listingRequest.getItemCondition());
        System.err.println("description " + listingRequest.getDescription());
        //TODO: DELETE DEBUG CODE

        Integer tmpListingID = new Random().nextInt(10000);
        ItemListing newItemListing = itemListingService.newItemListing(userID, listingRequest, tmpListingID);

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

        //here
        itemListingService.createS3BucketForUser(2L);

        return itemListingService.getNewListingID(tmpListingID);
    }

    // Add images to item listing
    @PostMapping(path = "/addImageToListing/{id}")
    public String addImageToListing(@PathVariable(value = "id") Long id,@RequestParam(value = "userId") Long userId,
                                    @RequestParam(value = "file") MultipartFile multipartFile, @RequestParam(value = "filename") String filename) {

        boolean image =  multipartFile == null;
        //TODO: DEBUG CODE
        System.err.println("ID: " + id);
        System.err.println("UserID: " + userId);
        System.err.println("filename: " + filename);
        System.err.println("multipartFile: " + image);
        //TODO: DELETE DEBUG CODE

        Integer tmpImageId = new Random().nextInt(10000);

        return itemImagesService.addImageToListing(id, 2L, multipartFile, filename, tmpImageId);
    }

    // Returns image link for a particular item
    @GetMapping(path = "/getListingImageLinks/{id}")
    public List<String> getListingImageLinks(@PathVariable(value = "id") Long id){
        return itemImagesService.getListingImageLinks(id);
    }

    // Modify item listing details
    @PutMapping(path = "/modifyItemListing/{id}")
    public ResponseEntity<?>modifyItemListing(@PathVariable(value = "id") Long listingID ,@RequestBody NewItemListingRequest listingRequest, BindingResult result){

        ResponseEntity<?> errorMap = mapValidationErrorService.MapValidationService(result);
        if(errorMap != null) return errorMap;

        itemListingService.updateItemListingDetails(listingID, listingRequest);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    // Deletes item listings and the corresponding images
    @DeleteMapping(path = "/deleteItemListing/{id}")
    public ResponseEntity<?>deleteItemListing(@PathVariable(value = "id") Long listingID, @RequestParam(value = "listingID") Long userID){

        try{
            itemListingService.deleteItemListingDetails(listingID, userID);
        }catch (Exception e){
            logger.error("Failed to delete image \n" + e);
            System.err.println("Failed to delete image \n" + e);
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
