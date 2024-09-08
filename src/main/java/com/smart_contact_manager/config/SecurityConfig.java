package com.smart_contact_manager.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.smart_contact_manager.service.impl.SecurityCustomUserDetailService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private SecurityCustomUserDetailService userDetailService;

	@Autowired
	OAuthAuthenticationSuccessHandler oAuthHandler;

    @Bean
    public DaoAuthenticationProvider authenticationProvider(){
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setUserDetailsService(userDetailService);
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder());
        return daoAuthenticationProvider;
    }

    @Bean
    public PasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(authorize -> authorize
                                .requestMatchers(new AntPathRequestMatcher("/login"), new AntPathRequestMatcher("/authenticate"), new AntPathRequestMatcher("/register"), new AntPathRequestMatcher("/css/**"), new AntPathRequestMatcher("/js/**")).permitAll()
                                .requestMatchers(new AntPathRequestMatcher("/user/**")).authenticated()
                                .anyRequest().permitAll()
                )
                .formLogin(formLogin -> formLogin
                                .loginPage("/login")
                                .loginProcessingUrl("/authenticate")
								.successForwardUrl("/user/profile")
                                .defaultSuccessUrl("/user/profile")
                                .failureUrl("/login?error=true")
                                .usernameParameter("email")
                                .passwordParameter("password")
                )
                .logout(logout -> logout
                                .logoutUrl("/logout")
                                .logoutSuccessUrl("/login?logout=true")
                )
                .csrf(csrf -> csrf.disable()); // Disable CSRF for simplicity, only for development environment

		// OAuth configurations
		http.oauth2Login(oauth->{
			oauth.loginPage("/login");
			oauth.successHandler(oAuthHandler);
            
		});  

        http.oauth2Login(oauth2 -> oauth2
        .userInfoEndpoint(userInfo -> userInfo
            .oidcUserService(oidcUserService())
        ));


        return http.build();
    }


    @Bean
    public OidcUserService oidcUserService() {
        return new OidcUserService();
    }
}
