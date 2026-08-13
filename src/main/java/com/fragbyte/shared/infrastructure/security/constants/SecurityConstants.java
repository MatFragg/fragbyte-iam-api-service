package com.fragbyte.shared.infrastructure.security.constants;

/**
 * Single source of truth for public paths (no authentication required). Consumed by both {@code
 * WebSecurityConfiguration} (Spring Security config) and {@code BearerAuthorizationRequestFilter}
 * (path matching).
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public final class SecurityConstants {

  public static final String[] PUBLIC_PATHS = {
    "/api/v1/authentication/sign-in",
    "/api/v1/authentication/sign-up",
    "/v3/api-docs/**",
    "/v3/api-docs.yaml",
    "/swagger-ui/**",
    "/swagger-ui.html",
    "/swagger-resources/**",
    "/webjars/**",
    "/api/v1/subscriptions/webhooks/**"
  };

  private SecurityConstants() {
    throw new AssertionError("Constants holder — not instantiable");
  }
}
