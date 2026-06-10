package springboot.project.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import springboot.project.dto.*;
import springboot.project.service.AuthService;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {
    
    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<ApiResponseDTO<Void>> register(@RequestBody RegisterRequestDTO requestDTO) {
        authService.registerStudent(requestDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponseDTO<>(201, "Student registered successfully", null));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> login(@RequestBody LoginRequestDTO request) {
        LoginResponseDTO response = authService.login(request);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Login successful", response));
    }

    @PostMapping("/refresh")
    public ResponseEntity<ApiResponseDTO<LoginResponseDTO>> refresh(@RequestBody TokenRefreshRequestDTO request) {
        LoginResponseDTO response = authService.refreshToken(request);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Token refreshed", response));
    }

    @PostMapping("/logout")
    public ResponseEntity<ApiResponseDTO<Void>> logout(@RequestHeader(value = "Authorization", required = false) String authHeader) {
        authService.logout(authHeader);
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Logout successful", null));
    }

    @PostMapping("/change-password")
    public ResponseEntity<ApiResponseDTO<Void>> changePassword(@RequestBody ChangePasswordRequestDTO request) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        authService.changePassword(request, auth.getName());
        return ResponseEntity.ok(new ApiResponseDTO<>(200, "Password changed successfully", null));
    }
}
