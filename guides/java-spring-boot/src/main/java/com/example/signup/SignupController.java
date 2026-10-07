package com.example.signup;

import java.util.Map;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestClientException;

@RestController
public class SignupController {

    public record SignupRequest(@NotBlank @Email String email) {}

    private final EmailVerificationClient verifier;

    public SignupController(EmailVerificationClient verifier) {
        this.verifier = verifier;
    }

    @PostMapping("/signup")
    public ResponseEntity<Map<String, Object>> signup(@Valid @RequestBody SignupRequest request) {
        EmailVerification result;
        try {
            result = verifier.verify(request.email().trim());
        } catch (RestClientException e) {
            result = null; // verification unavailable: do not block the signup
        }
        String decision = result == null ? "review" : result.decision();
        return switch (decision) {
            case "reject" -> ResponseEntity.badRequest().body(Map.of("error", "This email address cannot receive mail."));
            case "suggest" -> ResponseEntity.badRequest().body(Map.of("error", "Did you mean " + result.didYouMean() + "?"));
            // "accept" or "review": create the account; for "review", confirm the address by email
            default -> ResponseEntity.ok(Map.of("ok", true, "needsConfirmation", decision.equals("review")));
        };
    }
}
