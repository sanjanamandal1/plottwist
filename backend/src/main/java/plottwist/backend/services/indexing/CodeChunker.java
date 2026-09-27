package plottwist.backend.services.indexing;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.ai.document.Document;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import plottwist.backend.services.ai.RagSettings;

/**
 * Splits a source file into overlapping chunks sized for the embedding model.
 * <p>
 * Each chunk carries metadata (repo ID, file path, language) so the
 * vector store can filter by repository during retrieval.
 * </p>
 */
@Component
public class CodeChunker {

    @Value("${app.indexing.chunk-size:1200}")
    private int chunkSize;

    @Value("${app.indexing.chunk-overlap:200}")
    private int chunkOverlap;

    /**
     * Chunks a file's content into overlapping segments and wraps each
     * in a Spring AI {@link Document} with relevant metadata.
     */
    public List<Document> chunkFile(String repoId, String filePath, String content) {
        List<Document> docs = new ArrayList<>();
        if (content == null || content.isBlank()) return docs;

        String language = guessLanguage(filePath);
        int step = Math.max(1, chunkSize - chunkOverlap);

        for (int i = 0; i < content.length(); i += step) {
            int end = Math.min(i + chunkSize, content.length());
            String chunk = content.substring(i, end);

            Map<String, Object> metadata = Map.of(
                    RagSettings.METADATA_REPO_ID,   repoId,
                    RagSettings.METADATA_FILE_PATH,  filePath,
                    RagSettings.METADATA_LANGUAGE,   language
            );

            docs.add(new Document(chunk, metadata));

            if (end >= content.length()) break;
        }

        return docs;
    }

    private String guessLanguage(String path) {
        int dot = path.lastIndexOf('.');
        if (dot < 0) return "text";
        return path.substring(dot + 1).toLowerCase();
    }
}
