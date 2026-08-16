package com.fragbyte.iam.infrastructure.authorization.sfs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fragbyte.iam.domain.model.aggregates.User;
import com.fragbyte.iam.domain.model.valueobjects.AccountStatus;
import java.util.Collection;
import java.util.List;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

/**
 * Spring Security implementation of {@link UserDetails}.
 *
 * <p>Adapts the domain {@link User} aggregate into the representation required by Spring Security
 * during authentication and authorization.
 */
@Getter
@EqualsAndHashCode
public class UserDetailsImpl implements UserDetails {

  private final String userId;
  private final String email;

  @JsonIgnore private final String password;
  private final boolean accountNonExpired;
  private final boolean accountNonLocked;
  private final boolean credentialsNonExpired;
  private final boolean enabled;
  private final Collection<? extends GrantedAuthority> authorities;

  /**
   * User details implementation base constructor.
   *
   * @param userId the user identifier
   * @param email the email
   * @param password the user password
   * @param authorities the authorities
   */
  public UserDetailsImpl(
      String userId,
      String email,
      String password,
      Collection<? extends GrantedAuthority> authorities) {
    this(userId, email, password, authorities, true, true);
  }

  /**
   * User details implementation constructor with account state.
   *
   * @param userId the user identifier
   * @param email the email
   * @param password the user password
   * @param authorities the authorities
   * @param accountNonLocked whether the account is not locked
   * @param enabled whether the account is enabled
   */
  public UserDetailsImpl(
      String userId,
      String email,
      String password,
      Collection<? extends GrantedAuthority> authorities,
      boolean accountNonLocked,
      boolean enabled) {
    this.userId = userId;
    this.email = email;
    this.password = password;
    this.authorities = authorities;
    this.accountNonExpired = true;
    this.accountNonLocked = accountNonLocked;
    this.credentialsNonExpired = true;
    this.enabled = enabled;
  }

  /**
   * Creates a {@code UserDetailsImpl} from a domain {@link User}.
   *
   * @param user the authenticated user aggregate
   * @return a Spring Security user details instance
   */
  public static UserDetailsImpl build(User user) {
    var authorities =
        user.getAccessRoles().stream()
            .map(role -> new SimpleGrantedAuthority(role.asAuthority()))
            .map(authority -> (GrantedAuthority) authority)
            .toList();
    var isLocked = user.getAccountStatus() == AccountStatus.LOCKED;
    var isEnabled = user.getAccountStatus() == AccountStatus.ACTIVE;
    return new UserDetailsImpl(
        user.getUserId().value(),
        user.getEmail().email(),
        user.getPasswordHash().value(),
        authorities,
        !isLocked,
        isEnabled);
  }

  @Override
  public String getUsername() {
    return email;
  }
}
