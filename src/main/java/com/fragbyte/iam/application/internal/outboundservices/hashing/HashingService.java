package com.fragbyte.iam.application.internal.outboundservices.hashing;

/**
 * HashingService interface. This interface is used to encode and match passwords.
 *
 * <p>The hashing algorithm is an infrastructure concern — the domain does not know which algorithm
 * is used.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public interface HashingService {
  /**
   * Encode a password.
   *
   * @param rawPassword the password to encode
   * @return String the encoded password
   */
  String encode(CharSequence rawPassword);

  /**
   * Match a raw password with an encoded password.
   *
   * @param rawPassword the raw password
   * @param encodedPassword the encoded password
   * @return boolean true if the raw password matches the encoded password, false otherwise
   */
  boolean matches(CharSequence rawPassword, String encodedPassword);
}
