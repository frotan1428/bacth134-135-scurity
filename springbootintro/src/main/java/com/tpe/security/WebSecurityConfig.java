package com.tpe.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration//to make this class Configuration
@EnableWebSecurity// enabling security
@EnableGlobalMethodSecurity(prePostEnabled = true)// security will be enable in method base
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    /*
    1.AuthenticationManager
    2.AuthenticationProvider
    3.passEncode
     */

    @Autowired
    private UserDetailsService userDetailsService;

    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable(). //Disable Cross Site Request Forgery
                authorizeHttpRequests(). //check requests if they are authorized
                antMatchers("/",
                "/index.html",
//                "/register",
                "/css/*",
                "/js/*").permitAll(). //give permission to these paths-->no authenticatio/authorization
//                and().authorizeRequests().antMatchers("/students/**").hasRole("ADMIN").
        anyRequest(). //except other requests
                authenticated(). //authenticate
                and().
                httpBasic(); //use Basic Authentication for this
    }
    //Note!! /register add after postman you save



    @Bean
    public PasswordEncoder passwordEncoder(){//1234
        return new BCryptPasswordEncoder(10);// 4-31  // number between 4-31)
        //(4 will be weakest password and 31 strongest password(it take time) ,so normally middle round is 10
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){

        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setPasswordEncoder(passwordEncoder());
        authProvider.setUserDetailsService(userDetailsService);
        return authProvider;
    }

    @Override////Used to configure how authentication should be performed, such as providing user details, password encoding,
    // and defining custom authentication providers.
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.authenticationProvider(authenticationProvider());

    }
}
