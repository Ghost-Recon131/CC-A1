package rmit.cc.a1.transactionmicroservice.Transaction.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import rmit.cc.a1.transactionmicroservice.Transaction.model.Transactions;
import rmit.cc.a1.transactionmicroservice.Transaction.repository.TransactionRepository;

import java.util.List;

@Service
@AllArgsConstructor
public class TransactionServiceAdmin {

    private TransactionRepository transactionRepository;

    // Return all transactions
    public List<Transactions>getAllTransactions() {
        return transactionRepository.findAll();
    }

}
