package rmit.cc.a1.web;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import rmit.cc.a1.Account.services.MapValidationErrorService;
import rmit.cc.a1.ItemListing.model.ItemListing;
import rmit.cc.a1.ItemListing.repository.ItemListingRepository;
import rmit.cc.a1.ItemListing.requests.ModifyItemListingRequest;
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
    private ItemImagesService itemImagesService;

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

        itemListingService.createS3BucketForUser(userID);

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
    @PutMapping(path = "/modifyItemListing/{id}")
    public ResponseEntity<?>modifyItemListing(@PathVariable(value = "id") Long listingID , @RequestBody ModifyItemListingRequest listingRequest){

        itemListingService.updateItemListingDetails(listingID, listingRequest);

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    // Deletes item listings and the corresponding images
    @DeleteMapping(path = "/deleteItemListing/{id}")
    public ResponseEntity<?>deleteItemListing(@PathVariable(value = "id") Long listingID, @RequestParam(value = "userID") Long userID){

        try{
            itemListingService.deleteItemListingDetails(listingID, userID);
        }catch (Exception e){
            logger.error("Failed to delete image \n" + e);
            System.err.println("Failed to delete image \n" + e);
        }

        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

}
