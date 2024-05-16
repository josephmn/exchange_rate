package com.exchangerate.config;

import com.exchangerate.application.service.UserDetailServiceImpl;
import com.exchangerate.config.filter.JwtTokenValidator;
import com.exchangerate.util.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.annotation.web.configurers.HeadersConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity // para configurar por anotaciones
//@SecurityScheme(name="BearerAuth", scheme="bearer", type = SecuritySchemeType.HTTP, bearerFormat="JWT", in = SecuritySchemeIn.HEADER)
public class SecurityConfig {

    private final JwtUtils jwtUtils;

    @Autowired
    public SecurityConfig(JwtUtils jwtUtils) {
        this.jwtUtils = jwtUtils;
    }

    /** SECURITY FILTER CHAIN
     * Aqui se colocan las condiciones de seguridad
     * csrf -> Cross-site request forgery (La falsificación de petición en sitios cruzados) - vulnerabilidad Web
     * httpBasic -> usado solo para user y password
     * STATELESS -> no guardamos session en memoria
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable) //disable csrf
                .sessionManagement(sets -> sets.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(http -> {
                    http.requestMatchers(publicEndpoints()).permitAll();
                    http.anyRequest().authenticated();
                    //http.requestMatchers(HttpMethod.GET, "/api/v1/exchange/change").hasAnyRole("ADMIN","USER","INVITED","DEVELOPER");
                    //http.requestMatchers(HttpMethod.GET, "/api/v1/exchange/listsave").hasAnyRole("ADMIN","DEVELOPER");
                    //http.anyRequest().denyAll();
                })
                .headers(header -> {
                    header.frameOptions(HeadersConfigurer.FrameOptionsConfig::disable);
                })
                .addFilterBefore(new JwtTokenValidator(jwtUtils), BasicAuthenticationFilter.class)
                .build();
    }

    private RequestMatcher publicEndpoints() {
        return new OrRequestMatcher(
                new AntPathRequestMatcher("/v3/api-docs/**"),
                new AntPathRequestMatcher("/swagger-ui/**"),
                new AntPathRequestMatcher("/h2-console/**"),
                new AntPathRequestMatcher("/api/v1/auth/**")
        );
    }

    /*@Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .csrf(AbstractHttpConfigurer::disable)
                .httpBasic(Customizer.withDefaults())
                .sessionManagement(sets -> sets.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .build();
    }*/

    /** AUTHENTICATION MANAGER
     *
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /** AUTHENTICATION PROVIDER
     * Necesario: PasswordEncoder y UserDetailsService
     */
    @Bean
    public AuthenticationProvider authenticationProvider(UserDetailServiceImpl userDetailService) {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setPasswordEncoder(passwordEncoder());
        provider.setUserDetailsService(userDetailService);
        return provider;
    }

    /** PasswordEncoder
     * NoOpPasswordEncoder -> solo para test o pruebas
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
