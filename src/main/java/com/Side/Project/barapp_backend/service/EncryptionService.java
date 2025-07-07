package com.Side.Project.barapp_backend.service;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.stereotype.Service;

@Service
public class EncryptionService {

	@Value("${ENCRYPTION_SALT_ROUNDS:12}")
	private int saltRounds;

	private String salt;

	@PostConstruct
	public void postConstruct() {
		salt = BCrypt.gensalt(saltRounds);
	}

	public String encryptPassword(String password) {
		return BCrypt.hashpw(password, salt);
	}

	public boolean VerifyPassword(String password, String hash) {
		return BCrypt.checkpw(password, hash);
	}
}
