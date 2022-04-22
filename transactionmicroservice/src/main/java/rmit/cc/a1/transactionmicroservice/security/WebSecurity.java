package rmit.cc.a1.transactionmicroservice.security;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Overall config for website
        http.csrf().disable()
                .exceptionHandling()
                .and()
                .formLogin().permitAll()
                .and()
                .logout().permitAll()
                .and()
                .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and().exceptionHandling().accessDeniedPage("/403");

        // This section allows for public access of portions of the website
        http.authorizeRequests().antMatchers("/api/public/**").permitAll();

        // This allows anyone to access register for student account
        http.authorizeRequests().antMatchers("/api/Transactions/**").permitAll();
    }

}
