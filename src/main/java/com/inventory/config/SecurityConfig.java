package com.inventory.config;

import com.inventory.enums.Role;
import com.inventory.security.CustomPermissionEvaluator;
import com.inventory.services.UserDetailsServiceImpl;

import lombok.RequiredArgsConstructor;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.access.expression.method.DefaultMethodSecurityExpressionHandler;
import org.springframework.security.access.expression.method.MethodSecurityExpressionHandler;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import org.springframework.security.web.AuthenticationEntryPoint;
import jakarta.servlet.http.HttpServletResponse;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;


@Configuration
@EnableWebSecurity
@EnableMethodSecurity
public class SecurityConfig {

    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(@Lazy JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }
    
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
        .cors(Customizer.withDefaults()) // For react
            .csrf(AbstractHttpConfigurer::disable)
            .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
            .authorizeHttpRequests(auth -> auth
            	.requestMatchers("/auth/**").permitAll()
            	.requestMatchers("/admin/register-business").permitAll()
               .requestMatchers("/admin/**").hasRole("ADMIN")
  //              .requestMatchers("/admin/**").hasRole("FIRM_ADMIN")
               //.requestMatchers("/invoices/**").hasRole("ADMIN")

               //.requestMatchers(HttpMethod.PUT, "/employee/update/**").hasRole("ADMIN") // ✅ Add this
               
               //.requestMatchers("/invoices/**").hasRole("ADMIN")
               .requestMatchers(HttpMethod.GET, "/invoices/**").hasAnyRole("ADMIN", "BUSINESS_OWNER")
               .requestMatchers(HttpMethod.GET, "/invoices/send-invoice/**").hasAnyRole("ADMIN", "BUSINESS_OWNER")

               .requestMatchers(HttpMethod.POST, "/invoices/generate").hasRole("ADMIN")
               .requestMatchers(HttpMethod.PUT, "/invoices/mark-paid/**").hasAnyRole("ADMIN", "BUSINESS_OWNER")
               .requestMatchers("/invoices/**").hasAnyRole("ADMIN", "BUSINESS_OWNER")

               .requestMatchers(HttpMethod.OPTIONS, "/**").permitAll()


                .requestMatchers("/business/**").hasRole("BUSINESS_OWNER")
                
                .requestMatchers(HttpMethod.GET, "/employee/all").hasAnyRole("ADMIN", "BUSINESS_OWNER")
                .requestMatchers("/employee/**").hasRole("EMPLOYEE")
                .requestMatchers(HttpMethod.PUT, "/employee/update/**").hasAnyRole("ADMIN", "EMPLOYEE")
                
                .requestMatchers(HttpMethod.POST, "/api/stocks/record").hasRole("BUSINESS_OWNER")
                .requestMatchers(HttpMethod.GET, "/api/stocks/low-stock/**").hasAnyRole("BUSINESS_OWNER", "EMPLOYEE")
                .requestMatchers(HttpMethod.POST, "/api/purchase-orders/create").hasRole("BUSINESS_OWNER")
                .requestMatchers(HttpMethod.GET, "/products/all/**").hasAnyRole("ADMIN", "BUSINESS_OWNER", "EMPLOYEE")  // ✅ Add this
                .requestMatchers("/products/**").hasRole("BUSINESS_OWNER")

                .requestMatchers(HttpMethod.GET, "/api/suppliers/{businessId}").hasRole("BUSINESS_OWNER")

                .requestMatchers(HttpMethod.POST, "/api/suppliers").hasAnyRole("BUSINESS_OWNER", "ADMIN")

                .requestMatchers(HttpMethod.PUT, "/api/suppliers/**").hasRole("BUSINESS_OWNER")
                .requestMatchers(HttpMethod.DELETE, "/api/suppliers/**").hasRole("BUSINESS_OWNER")

                .anyRequest().authenticated()
            )
            .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

            .authenticationProvider(authenticationProvider(userDetailsService(), passwordEncoder()))
            .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        http.exceptionHandling()
        .authenticationEntryPoint(authenticationEntryPoint());
        return http.build();
    }
    
    @Bean
    public AuthenticationEntryPoint authenticationEntryPoint() {
        return (request, response, authException) -> {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\": \"Unauthorized or invalid token\"}");
        };
    }


    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailsService userDetailsService, PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

//    @Bean
//    public MethodSecurityExpressionHandler methodSecurityExpressionHandler(CustomPermissionEvaluator customEvaluator) {
//        var handler = new DefaultMethodSecurityExpressionHandler();
//        handler.setPermissionEvaluator(customEvaluator);
//        return handler;
//    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new UserDetailsServiceImpl();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
