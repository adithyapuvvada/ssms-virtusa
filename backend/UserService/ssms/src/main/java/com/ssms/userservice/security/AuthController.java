package com.ssms.userservice.security;

import com.ssms.userservice.entity.Shipper;
import com.ssms.userservice.entity.User;
import com.ssms.userservice.exception.UserException;
import com.ssms.userservice.repository.ShipperRepository;
import com.ssms.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/ssms/userservice/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final ShipperRepository shipperRepository;

    @PostMapping("/login")
    public AuthResponse login(@RequestBody AuthRequest request) {

        Authentication authenticate = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
    if (authenticate.isAuthenticated()){
        User user = userRepository.findByUsername(request.getUsername())
                .orElseThrow(() -> new UserException("User not found"));

        String role = authenticate.getAuthorities().stream().findFirst().get().getAuthority();

        // Setup default system fallbacks if the user has no company link (e.g. Admin)
        String country = "IN";
        String currencyCode = "INR";

        if (user.getCompanyId() != null) {
            // Find the supplier's company data to fetch their operational region
            Shipper shipper = shipperRepository.findById(user.getCompanyId())
                    .orElseThrow(() -> new RuntimeException("Associated shipper profile not found"));
            country = shipper.getCountry();
            currencyCode = shipper.getCurrencyCode();
        }
        String token = jwtService.generateToken(
                request.getUsername(),
                role,
                user.getCompanyId(),
                country,
                currencyCode);

        return new AuthResponse(token);
    }else {
        throw new RuntimeException("Invalid access");
    }
    }
}
