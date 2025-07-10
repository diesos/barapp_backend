package com.Side.Project.barapp_backend.api.models;

import com.Side.Project.barapp_backend.models.UserRole; // Assurez-vous d'importer le UserRole

public class UserResponse {
	private Long id;
	private String email;
	private UserRole role; // Ou String si vous préférez le nom du rôle

	public UserResponse(Long id, String email, UserRole role) {
		this.id = id;
		this.email = email;
		this.role = role;
	}

	// Getters
	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public UserRole getRole() {
		return role;
	}

}
