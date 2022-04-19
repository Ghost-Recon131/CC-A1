package rmit.cc.a1.ItemListing.services;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import rmit.cc.a1.AWSConfig.s3.service.S3Service;
import rmit.cc.a1.Account.model.Account;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.ItemListing.model.ItemListing;
import rmit.cc.a1.ItemListing.repository.ItemListingRepository;
import rmit.cc.a1.ItemListing.requests.NewItemListingRequest;
import rmit.cc.a1.utils.ItemCondition;

@Service
@AllArgsConstructor
public class ItemListingService {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private ItemListingRepository itemListingRepository;
    private S3Service s3Service;
    private AccountRepository accountRepository;
    private ItemImagesService itemImagesService;

    public Long getNewListingID(Integer tmpListingID){
        ItemListing newListing = itemListingRepository.getByTmpId(tmpListingID);
        Long newListingId = newListing.getId();

        newListing.setTmplistingID(null);
        itemListingRepository.save(newListing);

        return newListingId;
    }

    // Allows users to update their listing
    public void updateItemListingDetails(Long id, NewItemListingRequest request){
        ItemListing toUpdate = itemListingRepository.getById(id);
        ItemCondition condition = ItemCondition.valueOf(request.getItemCondition());

        if(request.getListingTitle() != null){
            toUpdate.setListingTitle(request.getListingTitle());
        }

        if(request.getPrice() != null){
            toUpdate.setPrice(request.getPrice());
        }

        if(request.getItemCondition() != null){
            toUpdate.setItemCondition(condition);
        }

        if(request.getDescription() != null){
            toUpdate.setDescription(request.getDescription());
        }

    }

    public ItemListing newItemListing(NewItemListingRequest listingRequest, Integer tmpListingID) {

        ItemListing newItemListing = null;

        // Need to convert to enum
        ItemCondition condition = ItemCondition.valueOf(listingRequest.getItemCondition());
        try {
            newItemListing = new ItemListing(
                    listingRequest.getId(),
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


}
