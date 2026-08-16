package com.fragbyte.iam.infrastructure.authorization.sfs.configuration;

import com.fragbyte.iam.infrastructure.authorization.sfs.pipeline.BearerAuthorizationRequestFilter;
import com.fragbyte.iam.infrastructure.hashing.bcrypt.BcryptHashingService;
import com.fragbyte.iam.infrastructure.token.jwt.BearerTokenService;
import com.fragbyte.shared.infrastructure.security.constants.SecurityConstants;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

import java.util.Arrays;
import java.util.List;

/**
 * Configures the application's Spring Security infrastructure.
 *
 * <p>Registers the security filter chain, authentication manager, password encoder, CORS
 * configuration, and JWT authorization filter.
 *
 * <p>The application uses stateless authentication based on bearer tokens, therefore HTTP sessions
 * are disabled.
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@SuppressWarnings("checkstyle:JavadocParagraph")
@Configuration
@EnableMethodSecurity
public class WebSecurityConfiguration {

  private final UserDetailsService userDetailsService;
  private final BearerTokenService tokenService;
  private final BcryptHashingService hashingService;
  private final AuthenticationEntryPoint unauthorizedRequestHandler;
  private final List<String> allowedOrigins;

  /**
   * Creates the JWT authorization filter.
   *
   * @return the filter responsible for authenticating requests using bearer tokens
   */
  @Bean
  public BearerAuthorizationRequestFilter authorizationRequestFilter() {
    return new BearerAuthorizationRequestFilter(tokenService);
  }

  /**
   * Exposes the application's {@link AuthenticationManager}.
   *
   * @param authenticationConfiguration the Spring Security configuration
   * @return the configured authentication manager
   * @throws Exception if the authentication manager cannot be created
   */
  @Bean
  public AuthenticationManager authenticationManager(
    AuthenticationConfiguration authenticationConfiguration) throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }

  /**
   * Exposes the password encoder used to hash and verify passwords.
   *
   * @return the configured {@link PasswordEncoder}
   */
  @Bean
  public PasswordEncoder passwordEncoder() {
    return hashingService;
  }

  /**
   * Configures the application's HTTP security.
   *
   * <p>The configured filter chain:
   *
   * <ul>
   *   <li>Enables CORS.
   *   <li>Disables CSRF protection.
   *   <li>Uses stateless session management.
   *   <li>Allows unrestricted access to public endpoints.
   *   <li>Requires authentication for every other request.
   *   <li>Registers the JWT authorization filter.
   * </ul>
   *
   * @param http the HTTP security builder
   * @return the configured security filter chain
   * @throws Exception if the security configuration cannot be built
   */
  @Bean
  public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    var allowedMethods = List.of("GET", "POST", "PUT", "PATCH", "DELETE");
    var allowedHeaders = List.of("*");
    http.cors(
      corsConfigurer ->
        corsConfigurer.configurationSource(
          request -> {
            var cors = new CorsConfiguration();
            cors.setAllowedOrigins(allowedOrigins);
            cors.setAllowedMethods(allowedMethods);
            cors.setAllowedHeaders(allowedHeaders);
            return cors;
          }));
    http.csrf(AbstractHttpConfigurer::disable)
      .exceptionHandling(
        exceptionHandling ->
          exceptionHandling.authenticationEntryPoint(unauthorizedRequestHandler))
      .sessionManagement(
        customizer -> customizer.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
      .authorizeHttpRequests(
        authorizeRequests ->
          authorizeRequests
            .requestMatchers(SecurityConstants.PUBLIC_PATHS)
            .permitAll()
            .anyRequest()
            .authenticated())
      .userDetailsService(userDetailsService);
    http.addFilterBefore(authorizationRequestFilter(), UsernamePasswordAuthenticationFilter.class);
    return http.build();
  }

  /**
   * Constructor for web security configuration.
   *
   * @param userDetailsService the user detail service.
   * @param tokenService the token service.
   * @param hashingService the hashing service.
   * @param authenticationEntryPoint the authentication entry point.
   * @param allowedOriginsCsv the allowed origins csv.
   */
  public WebSecurityConfiguration(
    @Qualifier("defaultUserDetailsService") UserDetailsService userDetailsService,
    BearerTokenService tokenService,
    BcryptHashingService hashingService,
    AuthenticationEntryPoint authenticationEntryPoint,
    @Value("${cors.allowed-origins}") String allowedOriginsCsv) {
    this.userDetailsService = userDetailsService;
    this.tokenService = tokenService;
    this.hashingService = hashingService;
    this.unauthorizedRequestHandler = authenticationEntryPoint;
    this.allowedOrigins =
      Arrays.stream(allowedOriginsCsv.split(","))
        .map(String::trim)
        .filter(s -> !s.isEmpty())
        .toList();
  }
}