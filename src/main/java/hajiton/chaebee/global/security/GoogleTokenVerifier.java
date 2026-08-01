package hajiton.chaebee.global.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import hajiton.chaebee.global.apiPayload.exception.ProjectException;


import hajiton.chaebee.domain.member.entity.MemberErrorCode;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Collections;

@Component
public class GoogleTokenVerifier {

    private final GoogleIdTokenVerifier verifier;

    public GoogleTokenVerifier(@Value("${google.client-id}") String webClientId) {
        this.verifier = new GoogleIdTokenVerifier.Builder(
                new NetHttpTransport(), GsonFactory.getDefaultInstance())
                .setAudience(Collections.singletonList(webClientId))
                .build();
    }

    public record GoogleUserInfo(String providerId, String email, String name) {}

    /** @return 구글 유저 정보 (providerId, email, name) */
    public GoogleUserInfo verify(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                throw new ProjectException(MemberErrorCode.INVALID_PROVIDER_TOKEN);
            }
            GoogleIdToken.Payload payload = idToken.getPayload();
            return new GoogleUserInfo(
                payload.getSubject(),
                payload.getEmail(),
                (String) payload.get("name")
            );
        } catch (Exception e) {
            throw new ProjectException(MemberErrorCode.INVALID_PROVIDER_TOKEN);
        }
    }
}