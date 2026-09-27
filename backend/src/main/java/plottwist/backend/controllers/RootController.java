package plottwist.backend.controllers;

import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Root endpoint returning API status and links.
 */
@RestController
public class RootController {

    @Value("${app.frontend.url:http://localhost:3000}")
    private String frontendUrl;

    @GetMapping("/")
    public Map<String, Object> root() {
        return Map.of(
            "name", "PlotTwist API",
            "status", "UP",
            "version", "1.0.0",
            "frontendUrl", frontendUrl,
            "message", "PlotTwist API is running. Access the web interface at " + frontendUrl
        );
    }
}
