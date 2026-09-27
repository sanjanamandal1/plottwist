package plottwist.backend.services.ai;

import java.util.List;

import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.ai.vectorstore.filter.FilterExpressionBuilder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

/**
 * Retrieves relevant code chunks from the vector store
 * for a given user query scoped to a specific repository.
 */
@Component
@RequiredArgsConstructor
public class CodeContextRetriever {

    private final VectorStore vectorStore;
    private final RagSettings ragSettings;

    /**
     * Finds the top-K most similar code snippets to the query within
     * the specified repository's indexed chunks.
     */
    public List<RetrievedContext> retrieve(String repoId, String query) {
        var filter = new FilterExpressionBuilder()
                .eq(RagSettings.METADATA_REPO_ID, repoId)
                .build();

        SearchRequest request = SearchRequest.builder()
                .query(query)
                .topK(ragSettings.getTopK())
                .similarityThreshold(ragSettings.getThreshold())
                .filterExpression(filter)
                .build();

        List<Document> docs = vectorStore.similaritySearch(request);

        return docs.stream()
                .map(doc -> new RetrievedContext(
                        (String) doc.getMetadata().getOrDefault(RagSettings.METADATA_FILE_PATH, "unknown"),
                        doc.getText()
                ))
                .toList();
    }
}
