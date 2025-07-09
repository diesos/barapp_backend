package com.Side.Project.barapp_backend.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;

import com.Side.Project.barapp_backend.security.JwtAuthFilter;

@Configuration
@EnableMethodSecurity
public class SecurityConfig {

	/**
	 * Configuration de la sécurité de l'application
	 * - Désactive CSRF
	 * - Autorise les requêtes vers /admin/** uniquement si authentifié
	 * - Autorise toutes les autres requêtes sans authentification
	 * - Désactive le formulaire de login et l'authentification HTTP basique
	 */

	@Autowired
	private JwtAuthFilter jwtAuthFilter;

	@Bean
	public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
		http
				.csrf(csrf -> csrf.disable())
				.authorizeHttpRequests(auth -> auth
						.requestMatchers("/admin/**").authenticated() // Requiert auth
						.anyRequest().permitAll() // TOUT le reste autorisé
				)
				.addFilterBefore(jwtAuthFilter,
						org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter.class)
				.formLogin(login -> login.disable())
				.httpBasic(basic -> basic.disable());
		return http.build();
	}

	@Bean
	public CorsFilter corsFilter() {
		CorsConfiguration corsConfig = new CorsConfiguration();
		corsConfig.setAllowedOrigins(Arrays.asList("http://localhost:5173"));
		corsConfig.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		corsConfig.setAllowedHeaders(Arrays.asList("*"));

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig);

		return new CorsFilter(source);
	}
}
