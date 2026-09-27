package plottwist.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.encrypt.Encryptors;
import org.springframework.security.crypto.encrypt.TextEncryptor;

/**
 * Provides a {@link TextEncryptor} for encrypting/decrypting
 * GitHub OAuth access tokens at rest using AES-256.
 */
@Configuration
public class CryptoConfig {

    @Value("${app.crypto.password}")
    private String password;

    @Value("${app.crypto.salt}")
    private String salt;

    @Bean
    public TextEncryptor textEncryptor() {
        return Encryptors.text(password, salt);
    }
}
