package rmit.cc.a1.transactionmicroservice.Transaction.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import rmit.cc.a1.transactionmicroservice.Transaction.model.Status;
import rmit.cc.a1.transactionmicroservice.Transaction.model.Transactions;

import java.util.List;

@Repository
public interface TransactionRepository extends JpaRepository<Transactions, Long> {

    Transactions getById(Long id);

//    @Query("Select a FROM Transactions a WHERE a.paypalPayID = ?1")
    Transactions getByPaypalPayID(String paypalPayID);

    Transactions getByPaypalToken(String token);

    List<Transactions> getAllByStatus(Status status);

    List<Transactions> getAllByBuyerID(Long id);

    Transactions getTransactionBySellerIDAndItemListingID(Long sellerID, Long itemListingID);

    Transactions getTransactionByBuyerIDAndAndItemListingID(Long buyerID, Long itemListingID);

    List<Transactions> findAllByBuyerIDAndSellerID(Long buyerID, Long sellerID);

    // Return all translations (admin feature)
    List<Transactions> findAll();


}
