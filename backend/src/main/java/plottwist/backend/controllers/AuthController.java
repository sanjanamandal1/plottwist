package plottwist.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import plottwist.backend.dto.UserResponse;
import plottwist.backend.security.CurrentUser;
import plottwist.backend.services.UserService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final CurrentUser currentUser;
    private final UserService userService;

    /** Returns the currently authenticated user's profile. */
    @GetMapping("/me")
    public UserResponse me() {
        return userService.toResponse(currentUser.require());
    }

    /** Logout is handled by Spring Security; this is a no-op endpoint. */
    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return ResponseEntity.noContent().build();
    }
}
