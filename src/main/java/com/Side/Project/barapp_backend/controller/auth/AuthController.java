package com.Side.Project.barapp_backend.controller.auth;

import com.Side.Project.barapp_backend.api.models.LoginBody;
import com.Side.Project.barapp_backend.api.models.RegistrationBody;
import com.Side.Project.barapp_backend.exception.UserAlreadyExist;
import com.Side.Project.barapp_backend.models.User;
import com.Side.Project.barapp_backend.service.UserService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.Map;

@RestController
@RequestMapping("/api/auth") // Bien /api/auth pour matcher avec ton SecurityConfig
public class AuthController {

	private final UserService userService;

	public AuthController(UserService userService) {
		this.userService = userService;
	}

	@PostMapping("/register")
	public ResponseEntity<?> registerUser(@Valid @RequestBody RegistrationBody registrationBody) {
		try {
			User createdUser = userService.registerUser(registrationBody);
			return ResponseEntity.status(HttpStatus.CREATED).body(
					Map.of(
							"message", "Inscription réussie",
							"email", createdUser.getEmail()));
		} catch (UserAlreadyExist e) {
			return ResponseEntity.status(HttpStatus.CONFLICT).body("Email déjà utilisé.");
		}
	}

	@PostMapping("/login")
	public ResponseEntity<?> loginUser(@Valid @RequestBody LoginBody loginBody) {
		String jwt = userService.loginUser(loginBody);
		if (jwt == null) {
			// Mauvais email ou mauvais password
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid credentials.");
		} else {
			return ResponseEntity.ok(jwt); // Tu peux renvoyer un objet type LoginResponse si tu préfères
		}
	}

	// Un endpoint pour tester le login/token plus tard si besoin
	@GetMapping("/me")
	public ResponseEntity<?> getCurrentUser(@RequestParam String email) {
		// Simple, à sécuriser plus tard (par exemple, récupérer l'utilisateur du JWT)
		return userService.getUserByEmail(email)
				.<ResponseEntity<Object>>map(ResponseEntity::ok)
				.orElse(ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found"));

	}
}
