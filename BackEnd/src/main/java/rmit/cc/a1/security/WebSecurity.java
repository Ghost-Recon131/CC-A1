package rmit.cc.a1.security;

import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.BeanIds;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import rmit.cc.a1.Account.services.AccountService;

@Configuration
@AllArgsConstructor
@EnableWebSecurity
public class WebSecurity extends WebSecurityConfigurerAdapter {

    @Autowired
    private final AccountService accountService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    private JwtAuthenticationEntryPoint jwtAuthenticationEntryPoint;

    @Override
    protected void configure(AuthenticationManagerBuilder auth) {
        auth.authenticationProvider(daoAuthenticationProvider());
    }

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(bCryptPasswordEncoder);
        provider.setUserDetailsService(accountService);
        return provider;
    }

    @Bean(name = BeanIds.AUTHENTICATION_MANAGER)
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return  new JwtAuthenticationFilter();
    }


    @Override
    protected void configure(HttpSecurity http) throws Exception {

        // Overall config for website
        http.csrf().disable()
                .exceptionHandling().authenticationEntryPoint(jwtAuthenticationEntryPoint)
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
        http.authorizeRequests().antMatchers("/api/RegisterLogin/**").permitAll();

        // This allows admins and students to access functions in AccountInfo API
        http.authorizeRequests().antMatchers("/api/StudentInfo/**").hasAnyAuthority("STUDENT", "ADMIN");

        // This only allows for authorised users to use these functions
        http.authorizeRequests().antMatchers("/api/authorised/**").hasAnyAuthority("STUDENT", "ADMIN");

        // This only allows for admins to use admin functions
        http.authorizeRequests().antMatchers("/api/admin/**").hasAuthority("ADMIN");

        // No restrictions on viewing FAQs
        http.authorizeRequests().antMatchers("/api/FAQ/public/**").permitAll();

        // Restrict adding and modifying FAQ to admin only
        http.authorizeRequests().antMatchers("/api/FAQ/admin/**").hasAuthority("ADMIN");


        // Add a filter to validate the tokens with every request
        http.addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class);
    }

}
