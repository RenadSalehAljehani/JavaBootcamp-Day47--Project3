package com.example.bankmanagementsystemproject3.Config;

import com.example.bankmanagementsystemproject3.Service.MyUserDetailsService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class ConfigurationSecurity {

    private final MyUserDetailsService myUserDetailsService;

    @Bean
    public DaoAuthenticationProvider daoAuthenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(myUserDetailsService);
        daoAuthenticationProvider.setPasswordEncoder(new BCryptPasswordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http.csrf().disable()
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
                .and()
                .authenticationProvider(daoAuthenticationProvider())
                .authorizeHttpRequests()
                .requestMatchers("/api/v1/employee/register"
                        , "/api/v1/customer/register").permitAll()
                .requestMatchers("/api/v1/employee/get"
                        , "/api/v1/employee/update").hasAuthority("Employee")
                .requestMatchers("/api/v1/customer/get"
                        , "/api/v1/customer/update"
                        , "/api/v1/account/view").hasAuthority("Customer")
                .requestMatchers("/api/v1/employee/delete"
                        , "/api/v1/customer/getAllCustomers"
                        , "/api/v1/account/create"
                        , "/api/v1/account/update/{accountId}"
                        , "/api/v1/account/delete/{accountId}"
                        , "/api/v1/account/active/{accountId}"
                        , "/api/v1/account/getAllAccounts"
                        , "/api/v1/block/accountId/{accountId}").hasAnyAuthority("Admin", "Employee")
                .requestMatchers("/api/v1/customer/delete").hasAnyAuthority("Admin", "Customer")
                .requestMatchers("/api/v1/deposit/accountId/{accountId}/amount/{amount}"
                        , "/api/v1/withdraw/accountId/{accountId}/amount/{amount}"
                        , "/api/v1/transfer/senderAccountId/{senderAccountId}/recipientAccountId/{recipientAccountId}/amount/{amount}").hasAnyAuthority("Admin", "Employee", "Customer")
                .requestMatchers("/api/v1/employee/getAllEmployees").hasAuthority("Admin")
                .anyRequest().authenticated()
                .and()
                .logout().logoutUrl("/api/v1/logout")
                .deleteCookies("JSESSIONID")
                .invalidateHttpSession(true)
                .and()
                .httpBasic();
        return http.build();
    }
}