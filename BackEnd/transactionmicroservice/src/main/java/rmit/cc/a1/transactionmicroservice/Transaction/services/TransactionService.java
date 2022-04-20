package rmit.cc.a1.transactionmicroservice.Transaction.services;

import com.paypal.api.payments.Transaction;
import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestParam;
import rmit.cc.a1.transactionmicroservice.Transaction.model.Status;
import rmit.cc.a1.transactionmicroservice.Transaction.model.Transactions;
import rmit.cc.a1.transactionmicroservice.Transaction.repository.TransactionRepository;
import rmit.cc.a1.transactionmicroservice.Transaction.requests.Order;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Service
@AllArgsConstructor
public class TransactionService {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private TransactionRepository transactionRepository;

    public void createNewTransaction(Order order, String paymentID, String paypalToken) {
        Transactions newTransaction = new Transactions(
                paymentID,
                paypalToken,
                order.getBuyerID(),
                order.getSellerID(),
                order.getItemListingID(),
                order.getPrice(),
                order.getCurrency(),
                null
        );

        transactionRepository.save(newTransaction);
    }

    // Sets the status to canceled
    public void cancelTransaction(String token){
        try{
            Transactions toCancel = transactionRepository.getByPaypalToken(token);
            assert toCancel != null;
            toCancel.setStatus(Status.CANCELED);
            transactionRepository.save(toCancel);
        }catch (Exception e){
            logger.error("Failed to set transaction status to 'CANCELED' " + e);
        }

    }

    // Sets the status to COMPLETED
    public void confirmTransaction(String paypalPayID){
        try{
            Transactions toConfirm = transactionRepository.getByPaypalPayID(paypalPayID);
            assert toConfirm != null;
            toConfirm.setStatus(Status.COMPLETED);
            transactionRepository.save(toConfirm);
        }catch (Exception e){
            logger.error("Failed to set transaction status to 'COMPLETED' " + e);
        }
    }

    // Sets the status to FAILED
    public void failTransaction(){
        try{
            List<Transactions> allTransactions = transactionRepository.getAllByStatus(Status.PENDING);
            LocalDate today = LocalDate.now();
            for (Transactions allTransaction : allTransactions) {
                LocalDate converted = convertToLocalDate(allTransaction.getTimeOfPurchase());

                // Checks if a transactions is still pending and is over 2 days old
                // When this condition is met, a transaction will be marked as failed
                if(allTransaction.getStatus() == Status.PENDING && converted.plusDays(2).isBefore(today)){
                    allTransaction.setStatus(Status.FAILED);
                }
            }

        }catch (Exception e){
            logger.error("Failed to set transaction status to 'FAILED' " + e);
        }
    }

    private LocalDate convertToLocalDate(Date dateToConvert) {
        return dateToConvert.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDate();
    }

    // Returns arraylist of successful user purchases
    public List<Transactions> getUserPurchases(Long userID){
        List<Transactions> userPurchases = transactionRepository.getAllByBuyerID(userID);
        userPurchases.removeIf(transactions -> transactions.getStatus() != Status.COMPLETED);
        
        return userPurchases;
    }

    // Returns transaction by transaction id
    public Transactions getById(Long id){
        return transactionRepository.getById(id);
    }

    // Return a transaction using sellerID and itemListingID
    public Transactions getTransactionBySellerIDAndItemListingID(Long sellerID, Long itemListingID){
        return transactionRepository.getTransactionBySellerIDAndItemListingID(sellerID, itemListingID);
    }

    // Return a transaction using buyerID and itemListingID
    public Transactions getTransactionByBuyerIDAndAndItemListingID(Long buyerID, Long itemListingID){
        return transactionRepository.getTransactionBySellerIDAndItemListingID(buyerID, itemListingID);
    }

    // Return all transactions between a buyer and a seller
    List<Transactions> findAllByBuyerIDAndSellerID(Long buyerID, Long sellerID){
        return transactionRepository.findAllByBuyerIDAndSellerID(buyerID, sellerID);
    }

}
