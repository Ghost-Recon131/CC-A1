package rmit.cc.a1.ItemListing.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import rmit.cc.a1.AWSConfig.s3.service.S3Service;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.ItemListing.model.ItemImages;
import rmit.cc.a1.ItemListing.model.ItemListing;
import rmit.cc.a1.ItemListing.repository.ItemImagesRepository;
import rmit.cc.a1.ItemListing.repository.ItemListingRepository;
import rmit.cc.a1.ItemListing.requests.ModifyItemListingRequest;
import rmit.cc.a1.ItemListing.requests.NewItemListingRequest;
import rmit.cc.a1.utils.ItemCondition;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemListingService {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private ItemListingRepository itemListingRepository;
    private S3Service s3Service;
    private AccountRepository accountRepository;
    private ItemImagesRepository itemImagesRepository;

    public Long getNewListingID(Integer tmpListingID){
        ItemListing newListing = itemListingRepository.getByTmpId(tmpListingID);
        Long newListingId = newListing.getId();

        newListing.setTmplistingID(null);
        itemListingRepository.save(newListing);

        return newListingId;
    }

    // Allows users to update their listing
    public void updateItemListingDetails(Long id, ModifyItemListingRequest request){
        ItemListing toUpdate = itemListingRepository.getById(id);

        if(request.getListingTitle() != null){
            toUpdate.setListingTitle(request.getListingTitle());
        }

        if(request.getPrice() != null){
            toUpdate.setPrice(request.getPrice());
        }

        if(request.getDescription() != null){
            toUpdate.setDescription(request.getDescription());
        }

        itemListingRepository.save(toUpdate);
    }

    public ItemListing newItemListing(Long userID, NewItemListingRequest listingRequest, Integer tmpListingID) {

        ItemListing newItemListing = null;

        // Need to convert to enum
        ItemCondition condition = ItemCondition.valueOf(listingRequest.getItemCondition());
        try {
            newItemListing = new ItemListing(
                    userID,
                    listingRequest.getListingTitle(),
                    listingRequest.getPrice(),
                    condition,
                    listingRequest.getDescription(),
                    tmpListingID
            );
            itemListingRepository.save(newItemListing);
            return newItemListing;
        } catch (Exception e) {
            logger.error("Error creating new listing" + e);
            System.err.println("Error creating new listing" + e);
        }

        return newItemListing;
    }

    // Create bucket for user if not already exist
    public void createS3BucketForUser(Long id){
        try{
            Account currentUser = accountRepository.getById(id);
            s3Service.createS3Bucket(currentUser.getUserRole().toString(), currentUser.getId(), currentUser.getUuid());
        } catch (Exception e) {
            logger.error("Error creating new S3 Bucket \n" + e);
            System.err.println("Error creating new S3 Bucket \n" + e);
        }
    }

    // Delete item listings
    public void deleteItemListingDetails(Long listingID, Long userID) {
        ItemListing toDelete = itemListingRepository.getById(listingID);
        List<ItemImages> allImages = itemImagesRepository.findByListingID(toDelete);

        // Delete all images and the then the listing
        for (ItemImages images : allImages) {
            if(images.getImageLink() != null){
                s3Service.deleteImage(accountRepository.getById(userID).getUserRole().toString(), userID, images.getImageLink());
                itemImagesRepository.deleteById(images.getId());
                itemListingRepository.deleteById(listingID);
            }
        }

        // when listing has no images
        itemListingRepository.deleteById(listingID);
    }


}
