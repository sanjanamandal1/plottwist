package plottwist.backend.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;

import plottwist.backend.security.GithubOAuth2UserService;
import lombok.RequiredArgsConstructor;

/**
 * Spring Security configuration for PlotTwist.
 * <p>
 * Uses GitHub OAuth2 login with session-based auth.
 * On success the user is redirected to the frontend callback page
 * which persists a lightweight auth cookie for middleware routing.
 * </p>
 */
@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final GithubOAuth2UserService githubOAuth2UserService;

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .cors(Customizer.withDefaults())
            .csrf(csrf -> csrf.disable())
            .sessionManagement(session ->
                session.sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
            )
            .exceptionHandling(eh ->
                eh.authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
            )
            .authorizeHttpRequests(auth -> auth
                .requestMatchers(
                    "/",
                    "/error",
                    "/actuator/health",
                    "/oauth2/**",
                    "/login/**"
                ).permitAll()
                .anyRequest().authenticated()
            )
            .oauth2Login(oauth -> oauth
                .userInfoEndpoint(info ->
                    info.userService(githubOAuth2UserService)
                )
                .successHandler(oauthSuccessHandler())
                .failureHandler(new SimpleUrlAuthenticationFailureHandler(
                    frontendUrl + "/login?error=oauth"
                ))
            )
            .logout(logout -> logout
                .logoutUrl("/api/auth/logout")
                .logoutSuccessHandler((req, res, auth) -> {
                    res.setStatus(HttpStatus.NO_CONTENT.value());
                })
                .deleteCookies("JSESSIONID")
            );

        return http.build();
    }

    /**
     * On successful OAuth2 login, redirect to the frontend callback page.
     * The callback page reads the session cookie and sets a lightweight
     * auth marker cookie for the Next.js middleware.
     */
    private AuthenticationSuccessHandler oauthSuccessHandler() {
        return (request, response, authentication) -> {
            response.sendRedirect(frontendUrl + "/auth/callback");
        };
    }
}
