package rmit.cc.a1.ItemListing.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rmit.cc.a1.ItemListing.model.ItemListing;
import rmit.cc.a1.ItemListing.repository.ItemListingRepository;
import rmit.cc.a1.ItemListing.requests.NewItemListingRequest;

import java.util.List;

@Service
@AllArgsConstructor
public class ItemListingService {

    private ItemListingRepository itemListingRepository;

    public Long getNewListingID(Long id, NewItemListingRequest listingRequest){
        Long listingID = null;
        List<ItemListing> itemListings = itemListingRepository.findAllByUserId(id);

        for (ItemListing itemListing : itemListings) {
            if (itemListing.getListingTitle().equals(listingRequest.getListingTitle())) {
                if(itemListing.getPrice() == listingRequest.getPrice()){
                    if(itemListing.getItemCondition().equals(listingRequest.getItemCondition())){
                        if(itemListing.getDescription().equals(listingRequest.getDescription())){
                            listingID = itemListing.getId();
                        }
                    }
                }
            }
        }

        return listingID;
    }

    // TODO
    public void updateItemListingDetails(Long id, NewItemListingRequest request){
        ItemListing toUpdate = itemListingRepository.getById(id);

        if(request.getListingTitle() != null){
            toUpdate.setListingTitle(request.getListingTitle());
        }

        if(request.getPrice() != null){
            toUpdate.setPrice(request.getPrice());
        }

        if(request.getItemCondition() != null){
            toUpdate.setItemCondition(request.getItemCondition());
        }

        if(request.getDescription() != null){
            toUpdate.setDescription(request.getDescription());
        }

    }

    // TODO
    public void updateItemListingImages(){

    }


}
