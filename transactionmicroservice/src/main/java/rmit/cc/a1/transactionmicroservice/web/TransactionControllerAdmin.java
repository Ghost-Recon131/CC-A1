package rmit.cc.a1.transactionmicroservice.web;

import lombok.AllArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.*;
import rmit.cc.a1.transactionmicroservice.Transaction.model.Transactions;
import rmit.cc.a1.transactionmicroservice.Transaction.services.TransactionServiceAdmin;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/Transactions/admin")
@AllArgsConstructor
public class TransactionControllerAdmin {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private TransactionServiceAdmin transactionServiceAdmin;

    // Create new transaction
    @GetMapping(path = "/admin/")
    public List<Transactions> getAllTransactions(){
        List<Transactions> allTransactions = new ArrayList<>();

        try{
            allTransactions = transactionServiceAdmin.getAllTransactions();
        }catch (Exception e){
            logger.error("Error creating new transaction", e);
            System.err.println("Error creating new transaction\n" +  e);
        }

        return allTransactions;
    }


}
