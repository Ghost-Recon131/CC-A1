package rmit.cc.a1.transactionmicroservice.PayPal.service;

import com.paypal.api.payments.*;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.paypal.base.rest.APIContext;
import rmit.cc.a1.transactionmicroservice.Transaction.requests.Order;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

@Service
public class PayPalAPIService {

    //TODO Fix here
    @Autowired
    private APIContext apiContext;

    // Create new payment to PayPal API
    public Payment createPayment(Order order, String successURL, String cancelURL) throws PayPalRESTException {
        Amount price = new Amount();
        price.setCurrency(order.getCurrency());

        Double totalDue = new BigDecimal(order.getPrice()).setScale(2, RoundingMode.HALF_UP).doubleValue();
        price.setTotal(String.format("%.2f", totalDue));

        Transaction transaction = new Transaction();
        transaction.setDescription("Payment for item ordered on web store");
        transaction.setAmount(price);

        List<Transaction> transactions = new ArrayList<>();
        transactions.add(transaction);

        Payer buyer = new Payer();
        buyer.setPaymentMethod("PAYPAL");

        Payment payment = new Payment();
        payment.setIntent("SALE");
        payment.setPayer(buyer);
        payment.setTransactions(transactions);

        RedirectUrls redirectUrls = new RedirectUrls();
        redirectUrls.setCancelUrl(cancelURL);
        redirectUrls.setReturnUrl(successURL);
        payment.setRedirectUrls(redirectUrls);

        return payment.create(apiContext);
    }

    // Sends payment details to PayPal to finalise payment
    public Payment confirmPayment(String paymentID, String buyerID) throws PayPalRESTException{
        Payment payment = new Payment();
        payment.setId(paymentID);
        PaymentExecution paymentExecute = new PaymentExecution();
        paymentExecute.setPayerId(buyerID);
        return payment.execute(apiContext, paymentExecute);
    }

}
