package plottwist.backend.services.ai;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;

/**
 * Builds the system + user prompt for the AI, injecting retrieved
 * code context and the conversation history.
 */
@Component
public class ChatPromptBuilder {

    private static final String SYSTEM_PROMPT = """
            You are PlotTwist — an expert coding assistant. You answer questions about
            a specific GitHub repository. Use ONLY the code context provided below to
            answer. If you reference code, cite the file path in brackets like [src/Foo.java].
            If the context is insufficient, say so honestly.
            """;

    /**
     * Assembles the full prompt with system instructions, code context,
     * and the user's question.
     */
    public String build(String userMessage, List<RetrievedContext> contexts) {
        String contextBlock = contexts.stream()
                .map(ctx -> "--- " + ctx.filePath() + " ---\n" + ctx.content())
                .collect(Collectors.joining("\n\n"));

        return SYSTEM_PROMPT + "\n\n"
                + "## Code Context\n\n" + contextBlock + "\n\n"
                + "## Question\n\n" + userMessage;
    }
}
