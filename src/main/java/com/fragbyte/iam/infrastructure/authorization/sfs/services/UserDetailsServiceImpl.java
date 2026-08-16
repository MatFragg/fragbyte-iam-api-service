package com.fragbyte.iam.infrastructure.authorization.sfs.services;

import com.fragbyte.iam.domain.model.valueobjects.Email;
import com.fragbyte.iam.infrastructure.authorization.sfs.model.UserDetailsImpl;
import com.fragbyte.iam.infrastructure.persistence.repositories.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Default implementation of {@link UserDetailsService} backed by the {@link UserRepository}.
 *
 * <p>Used by Spring Security during authentication to load user information from the persistence
 * layer and adapt it into a {@link UserDetails} instance.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
@Service(value = "defaultUserDetailsService")
public class UserDetailsServiceImpl implements UserDetailsService {

  private final UserRepository userRepository;

  /**
   * Creates a new user details service.
   *
   * @param userRepository the repository used to retrieve users by email
   */
  public UserDetailsServiceImpl(UserRepository userRepository) {
    this.userRepository = userRepository;
  }

  /**
   * Loads a user by email address.
   *
   * <p>Spring Security invokes this method during authentication to retrieve the corresponding
   * {@link UserDetails}.
   *
   * @param email the user's email address
   * @return the user details associated with the specified email
   * @throws UsernameNotFoundException if no user exists with the given email
   */
  @Override
  @Transactional(readOnly = true)
  public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    var user =
        userRepository
            .findByEmail(new Email(email))
            .orElseThrow(
                () -> new UsernameNotFoundException("User not found with email: " + email));
    return UserDetailsImpl.build(user);
  }
}
