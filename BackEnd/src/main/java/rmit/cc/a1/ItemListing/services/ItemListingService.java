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

    public Long getNewListingID(Integer tmpListingID){
        ItemListing newListing = itemListingRepository.getByTmpId(tmpListingID);
        Long newListingId = newListing.getId();

        newListing.setTmplistingID(null);
        itemListingRepository.save(newListing);

        return newListingId;
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
