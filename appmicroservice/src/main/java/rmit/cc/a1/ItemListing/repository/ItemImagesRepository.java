package rmit.cc.a1.ItemListing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rmit.cc.a1.ItemListing.model.ItemImages;
import rmit.cc.a1.ItemListing.model.ItemListing;

import java.util.List;

@Repository
public interface ItemImagesRepository extends JpaRepository<ItemImages, Long> {

    ItemImages getById(Long id);

    // Get links to all images for a single listing
    @Query("SELECT a FROM ItemImages a WHERE a.itemListing = ?1")
    List<ItemImages> findByListingID(ItemListing itemListing);

    @Query("SELECT a FROM ItemImages a WHERE a.tmpImageId = ?1")
    ItemImages getByTmpId(Integer id);

}
