package rmit.cc.a1.ItemListing.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import rmit.cc.a1.AWSConfig.s3.service.S3Service;
import rmit.cc.a1.Account.repository.AccountRepository;
import rmit.cc.a1.ItemListing.model.ItemImages;
import rmit.cc.a1.ItemListing.model.ItemListing;
import rmit.cc.a1.ItemListing.repository.ItemImagesRepository;
import rmit.cc.a1.ItemListing.repository.ItemListingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemImagesService {

    private ItemImagesRepository itemImagesRepository;
    private ItemListingRepository itemListingRepository;
    private AccountRepository accountRepository;
    private S3Service s3Service;

    // Return arraylist of image links
    public List<String> getListingImageLinks(Long id){
        List<String> imageLinks = new ArrayList<>();

        List<ItemImages> itemImages = itemImagesRepository.findByListingID(itemListingRepository.getById(id));

        for (ItemImages itemImage : itemImages) {
            imageLinks.add(itemImage.getImageLink());

        }
        return imageLinks;
    }

    // Add images to existing listing
    public String addImageToListing(Long id, Long userId, MultipartFile multipartFile, String fileName, Integer tmpImageId){
        ItemListing toAddImage = itemListingRepository.getById(id);

        // TODO: Adds images to item listing via S3
        ItemImages newImage = new ItemImages(
                toAddImage,
                null,
                null,
                tmpImageId
        );
        itemImagesRepository.save(newImage);
        Long imageid = getImageID(tmpImageId);

        // final MultipartFile multipartFile, Long imageID, Long listingID, String s3BucketName
        return s3Service.saveImage(multipartFile, fileName, imageid, id, accountRepository.getById(userId));
    }

    // Returns ID of new image then clears the tmpImageId
    public Long getImageID(Integer tmpListingID){
        ItemImages newImage = itemImagesRepository.getByTmpId(tmpListingID);
        Long newImageId = newImage.getId();

        newImage.setTmpImageId(null);
        itemImagesRepository.save(newImage);

        return newImageId;
    }


}
