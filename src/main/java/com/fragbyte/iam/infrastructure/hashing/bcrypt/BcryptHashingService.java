package com.fragbyte.iam.infrastructure.hashing.bcrypt;

import com.fragbyte.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This interface is a marker interface for the bcrypt hashing service. It extends the {@link
 * HashingService} and {@link PasswordEncoder} interfaces. This interface is used to inject the
 * Bcrypt hashing service in the {@link com.fragbyte.iam.infrastructure.hashing.bcrypt.services.HashingServiceImpl} class.
 *
 * @author FragByte Development team.
 * @since 2026-13-08
 */
public interface BcryptHashingService extends HashingService, PasswordEncoder {}
