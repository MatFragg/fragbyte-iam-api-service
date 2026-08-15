package com.hampcoders.glottia.platform.api.iam.infrastructure.authorization.sfs.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.hampcoders.glottia.platform.api.iam.domain.model.aggregates.User;
import com.hampcoders.glottia.platform.api.shared.interfaces.rest.security.CurrentUserDetails;
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
public class UserDetailsImpl implements UserDetails, CurrentUserDetails {

  private final String userId;
  private final String username;

  @JsonIgnore private final String password;
  private final String subjectId;
  private final String role;
  private final String profileId;
  private final boolean accountNonExpired;
  private final boolean accountNonLocked;
  private final boolean credentialsNonExpired;
  private final boolean enabled;
  private final Collection<? extends GrantedAuthority> authorities;

  /**
   * User details impl base constructor with unassigned role.
   *
   * @param userId the user identifier
   * @param username the username
   * @param password the user password
   * @param authorities the authorities
   */
  public UserDetailsImpl(
      String userId,
      String username,
      String password,
      Collection<? extends GrantedAuthority> authorities) {
    this(userId, username, password, null, "UNASSIGNED", null, authorities);
  }

  /**
   * User details implementation base constructor.
   *
   * @param userId the user identifier
   * @param username the username
   * @param password the user password
   * @param subjectId the subject identifier
   * @param role the business role
   * @param profileId the profile identifier
   * @param authorities the authorities
   */
  public UserDetailsImpl(
      String userId,
      String username,
      String password,
      String subjectId,
      String role,
      String profileId,
      Collection<? extends GrantedAuthority> authorities) {
    this.userId = userId;
    this.username = username;
    this.password = password;
    this.subjectId = subjectId;
    this.role = role;
    this.profileId = profileId;
    this.authorities = authorities;
    this.accountNonExpired = true;
    this.accountNonLocked = true;
    this.credentialsNonExpired = true;
    this.enabled = true;
  }

  /**
   * Creates a {@code UserDetailsImpl} from a domain {@link User}.
   *
   * @param user the authenticated user aggregate
   * @return a Spring Security user details instance
   */
  public static UserDetailsImpl build(User user) {
    var authority = new SimpleGrantedAuthority(user.getAccessRole().asAuthority());
    return new UserDetailsImpl(
        user.getId().value(), user.getEmail(), user.getPassword(), List.of(authority));
  }
}
