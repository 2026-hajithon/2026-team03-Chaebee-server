package hajiton.chaebee.security;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import hajiton.chaebee.apiPayload.exception.ProjectException;


import hajiton.chaebee.member.domain.MemberErrorCode;
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

    /** @return 구글 고유 사용자 ID (providerId로 사용) */
    public String verify(String idTokenString) {
        try {
            GoogleIdToken idToken = verifier.verify(idTokenString);
            if (idToken == null) {
                // 💡 ErrorCode -> MemberErrorCode 로 변경 완료
                throw new ProjectException(MemberErrorCode.INVALID_PROVIDER_TOKEN);
            }
            return idToken.getPayload().getSubject();
        } catch (Exception e) {
            // 💡 여기도 MemberErrorCode 로 변경 완료
            throw new ProjectException(MemberErrorCode.INVALID_PROVIDER_TOKEN);
        }
    }
}