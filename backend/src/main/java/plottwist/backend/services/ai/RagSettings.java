package plottwist.backend.services.ai;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/** Centralised constants and tunables for the RAG pipeline. */
@Component
public class RagSettings {

    public static final String METADATA_REPO_ID  = "repo_id";
    public static final String METADATA_FILE_PATH = "file_path";
    public static final String METADATA_LANGUAGE  = "language";

    @Value("${app.rag.similarity-top-k:8}")
    private int topK;

    @Value("${app.rag.similarity-threshold:0.72}")
    private double threshold;

    public int getTopK()         { return topK; }
    public double getThreshold() { return threshold; }
}
