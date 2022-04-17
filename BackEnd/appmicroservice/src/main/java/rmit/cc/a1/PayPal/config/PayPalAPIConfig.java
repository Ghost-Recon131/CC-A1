package rmit.cc.a1.PayPal.config;

import java.util.HashMap;
import java.util.Map;

import com.paypal.base.rest.APIContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import com.paypal.base.rest.OAuthTokenCredential;
import com.paypal.base.rest.PayPalRESTException;
import org.springframework.context.annotation.Configuration;

@Configuration
public class PayPalAPIConfig {

    @Value("${paypal.client.id}")
    private String clientID;
    @Value("${paypal.client.secret}")
    private String clientSecret;
    @Value("${paypal.mode}")
    private String PayPalMode;

    @Bean
    public Map<String, String> paypalSdkConfig() {
        Map<String, String> map = new HashMap<>();
        map.put("mode", PayPalMode);
        return map;
    }

    @Bean
    public OAuthTokenCredential oAuthTokenCredential() {
        return new OAuthTokenCredential(clientID, clientSecret, paypalSdkConfig());
    }

    @Bean
    public APIContext apiContext() throws PayPalRESTException {
        APIContext apiContext = new APIContext(oAuthTokenCredential().getAccessToken());
        apiContext.setConfigurationMap(paypalSdkConfig());
        return apiContext;
    }


}
