package com.hampcoders.glottia.platform.api.iam.infrastructure.hashing.bcrypt;

import com.hampcoders.glottia.platform.api.iam.application.internal.outboundservices.hashing.HashingService;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * This interface is a marker interface for the bcrypt hashing service. It extends the {@link
 * HashingService} and {@link PasswordEncoder} interfaces. This interface is used to inject the
 * Bcrypt hashing service in the {@link HashingServiceImpl} class.
 */
public interface BcryptHashingService extends HashingService, PasswordEncoder {}
