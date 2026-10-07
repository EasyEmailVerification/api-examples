package com.example.signup;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public record EmailVerification(
        String email,
        String result,
        String reason,
        boolean disposable,
        @JsonProperty("accept_all") boolean acceptAll,
        boolean role,
        @JsonProperty("safe_to_send") boolean safeToSend,
        @JsonProperty("did_you_mean") String didYouMean) {

    public String decision() {
        if (didYouMean != null && !didYouMean.isEmpty()) return "suggest"; // ask the user: did you mean ...?
        if ("valid".equals(result) && safeToSend) return "accept";
        if ("invalid".equals(result)) return "reject";
        return "review"; // unknown, catch-all or disposable: your policy decides
    }
}
