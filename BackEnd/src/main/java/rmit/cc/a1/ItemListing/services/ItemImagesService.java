package rmit.cc.a1.ItemListing.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rmit.cc.a1.ItemListing.model.ItemImages;
import rmit.cc.a1.ItemListing.repository.ItemImagesRepository;
import rmit.cc.a1.ItemListing.repository.ItemListingRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@AllArgsConstructor
public class ItemImagesService {

    private ItemImagesRepository itemImagesRepository;
    private ItemListingRepository itemListingRepository;

    // Return arraylist of image links
    public List<String> getListingImageLinks(Long id){
        List<String> imageLinks = new ArrayList<>();

        List<ItemImages> itemImages = itemImagesRepository.findByListingID(itemListingRepository.getById(id));

        for (ItemImages itemImage : itemImages) {
            imageLinks.add(itemImage.getImageLink());

        }
        return imageLinks;
    }

}
