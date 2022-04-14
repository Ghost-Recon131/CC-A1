package rmit.cc.a1.ItemListing.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rmit.cc.a1.ItemListing.model.ItemListing;

import java.util.List;

@Repository
public interface ItemListingRepository extends JpaRepository<ItemListing, Long> {

    ItemListing getById(Long id);

    // Get all listings for a user
    @Query("SELECT a FROM ItemListing a WHERE a.account = ?1")
    List<ItemListing> findByUserId(Long id);

    // Delete a listing
    void deleteById(Long id);

}
