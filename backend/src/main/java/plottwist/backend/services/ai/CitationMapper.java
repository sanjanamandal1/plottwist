package plottwist.backend.services.ai;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Component;

import plottwist.backend.dto.CitationDto;

/**
 * Extracts file-path citations from the AI response text.
 * <p>
 * The model is instructed to cite files using brackets like {@code [src/Foo.java]}.
 * This mapper finds those references and maps them back to the retrieved
 * context snippets.
 * </p>
 */
@Component
public class CitationMapper {

    private static final Pattern CITATION_PATTERN = Pattern.compile("\\[([^\\]]+\\.[a-zA-Z]+)]");

    /**
     * Scans the AI response for bracketed file references and matches them
     * against the retrieved code contexts to build citation objects.
     */
    public List<CitationDto> extractCitations(String response, List<RetrievedContext> contexts) {
        List<CitationDto> citations = new ArrayList<>();
        Matcher matcher = CITATION_PATTERN.matcher(response);

        while (matcher.find()) {
            String citedPath = matcher.group(1).trim();
            contexts.stream()
                    .filter(ctx -> ctx.filePath().endsWith(citedPath) || ctx.filePath().equals(citedPath))
                    .findFirst()
                    .ifPresent(ctx -> {
                        // Avoid duplicate citations for the same file
                        boolean alreadyCited = citations.stream()
                                .anyMatch(c -> c.filePath().equals(ctx.filePath()));
                        if (!alreadyCited) {
                            String snippet = ctx.content().length() > 300
                                    ? ctx.content().substring(0, 300) + "…"
                                    : ctx.content();
                            citations.add(new CitationDto(ctx.filePath(), snippet));
                        }
                    });
        }
        return citations;
    }
}
