package rmit.cc.a1.transactionmicroservice.web;

import com.paypal.api.payments.Links;
import com.paypal.base.rest.PayPalRESTException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import rmit.cc.a1.transactionmicroservice.Transaction.model.Transactions;
import rmit.cc.a1.transactionmicroservice.Transaction.requests.Order;
import com.paypal.api.payments.Payment;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.*;
import rmit.cc.a1.transactionmicroservice.PayPal.service.PayPalAPIService;
import rmit.cc.a1.transactionmicroservice.Transaction.services.TransactionService;

import java.util.ArrayList;
import java.util.List;

@RestController
@CrossOrigin(origins = "http://localhost:3000")
@RequestMapping("/api/Transactions")
@AllArgsConstructor
public class TransactionController {

    private final Logger logger = LogManager.getLogger(this.getClass());
    private TransactionService transactionService;

    private PayPalAPIService paypalAPIService;

    public static final String successURL = "http://localhost:8081/api/Transactions/success";
    public static final String cancelURL = "http://localhost:8081/api/Transactions/cancel";

    // Create new transaction
    // Log into sandbox account: https://www.sandbox.paypal.com/mep/dashboard
    @PostMapping(path = "/createPayment")
    public String createNewTransaction(@RequestBody Order order){
        String redirectLink = "redirect:/";
        String PaymentID = null;
        try{
            Payment paypalPayment = paypalAPIService.createPayment(order, successURL, cancelURL);
            PaymentID = paypalPayment.getId();
            for(Links link:paypalPayment.getLinks()) {
                if(link.getRel().equals("approval_url")) {
                    redirectLink = "redirect:/"+link.getHref();
                }
            }
        }catch (PayPalRESTException e) {
            logger.error("Error creating PayPal payment", e);
        }

        try{
            int lastOccurance = redirectLink.lastIndexOf("token=");
            String paypalToken = redirectLink.substring(lastOccurance+6);
            transactionService.createNewTransaction(order, PaymentID, paypalToken);
        }catch (Exception e){
            logger.error("Error creating new transaction in database", e);
        }

        return redirectLink;
    }

    //path = "/cancel",
    @GetMapping(value = cancelURL)
    public String cancelPay(@RequestParam("token") String token) {
        transactionService.cancelTransaction(token);
        return "Payment was cancelled";
    }

    //path = "/success",
    @GetMapping(value = successURL)
    public String successPay(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String PayerID) {
        try {
            Payment payment = paypalAPIService.confirmPayment(paymentId, PayerID);
            if (payment.getState().equals("approved")) {
                transactionService.confirmTransaction(PayerID);
                return "success";
            }else{
                transactionService.failTransaction();
            }
        }catch (PayPalRESTException e) {
            logger.error("Error processing payment on PayPal API", e);
        }
        return "redirect:/";
    }

    // Get list of purchases for a user
    @GetMapping(path = "/getUserPurchases")
    public List<Transactions> getUserPurchases(@RequestParam("userID") Long userID){
        List<Transactions> userPurchases = new ArrayList<>();

        try{
            userPurchases = transactionService.getUserPurchases(userID);
        }catch (Exception e){
            logger.error("Error getting user purchases \n", e);
        }

        return userPurchases;
    }

    //TODO: TEMP PATHS
    @RequestMapping("/success")
    public String PayPalSuccess(@RequestParam("paymentId") String paymentId, @RequestParam("PayerID") String PayerID) {
        try {
            Payment payment = paypalAPIService.confirmPayment(paymentId, PayerID);
            if (payment.getState().equals("approved")) {
                transactionService.confirmTransaction(paymentId);
                return "success";
            }else{
                transactionService.failTransaction();
            }
        }catch (PayPalRESTException e) {
            logger.error("Error processing payment on PayPal API", e);
        }
        return "Paypal Payment was successful";
    }

    @RequestMapping("/cancel")
    public String PayPalCancel(@RequestParam("token") String token) {
        transactionService.cancelTransaction(token);
        return "Paypal Payment was canceled";
    }
    //TODO: Above are temp methods

}
