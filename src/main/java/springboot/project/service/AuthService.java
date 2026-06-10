package springboot.project.service;

import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springboot.project.dto.ChangePasswordRequestDTO;
import springboot.project.dto.LoginRequestDTO;
import springboot.project.dto.LoginResponseDTO;
import springboot.project.dto.RegisterRequestDTO;
import springboot.project.dto.TokenRefreshRequestDTO;
import springboot.project.entity.RefreshToken;
import springboot.project.entity.TokenBlacklist;
import springboot.project.entity.User;
import springboot.project.repository.RefreshTokenRepository;
import springboot.project.repository.TokenBlacklistRepository;
import springboot.project.repository.UserRepository;
import springboot.project.security.JwtUtil;

import java.util.Date;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;
    private final RefreshTokenRepository refreshTokenRepository;
    private final TokenBlacklistRepository tokenBlacklistRepository;
    
    public void registerStudent(RegisterRequestDTO requestDTO) {
        if (userRepository.existsByUsername(requestDTO.getUsername())) {
            throw new RuntimeException("Username is already taken!");
        }

        User user = new User();
        user.setUsername(requestDTO.getUsername());
        user.setPassword(passwordEncoder.encode(requestDTO.getPassword()));
        user.setRole("ROLE_STUDENT");
        user.setActive(true);

        userRepository.save(user);
    }

    public LoginResponseDTO login(LoginRequestDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String accessToken = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        RefreshToken refreshTokenEntity = new RefreshToken();
        refreshTokenEntity.setToken(refreshToken);
        refreshTokenEntity.setUsername(request.getUsername());
        refreshTokenEntity.setExpirationTime(jwtUtil.extractExpiration(refreshToken));
        refreshTokenRepository.save(refreshTokenEntity);

        return new LoginResponseDTO(accessToken, refreshToken);
    }

    public LoginResponseDTO refreshToken(TokenRefreshRequestDTO request) {
        String reqRefreshToken = request.getRefreshToken();
        RefreshToken tokenInDb = refreshTokenRepository.findByToken(reqRefreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh token is not in database!"));

        if (tokenInDb.getExpirationTime().before(new Date())) {
            refreshTokenRepository.delete(tokenInDb);
            throw new RuntimeException("Refresh token was expired. Please make a new signin request");
        }

        UserDetails userDetails = userDetailsService.loadUserByUsername(tokenInDb.getUsername());
        String accessToken = jwtUtil.generateToken(userDetails);
        
        return new LoginResponseDTO(accessToken, reqRefreshToken);
    }

    public void logout(String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            Date expirationDate = jwtUtil.extractExpiration(jwt);
            TokenBlacklist blacklistedToken = new TokenBlacklist();
            blacklistedToken.setToken(jwt);
            blacklistedToken.setExpirationTime(expirationDate);
            tokenBlacklistRepository.save(blacklistedToken);
        }
    }

    public void changePassword(ChangePasswordRequestDTO request, String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (!passwordEncoder.matches(request.getOldPassword(), user.getPassword())) {
            throw new RuntimeException("Wrong old password");
        }

        user.setPassword(passwordEncoder.encode(request.getNewPassword()));
        userRepository.save(user);
    }
}
