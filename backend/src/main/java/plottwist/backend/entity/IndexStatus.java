package plottwist.backend.entity;

/**
 * Tracks the lifecycle of a repository's RAG indexing pipeline.
 *
 * <ul>
 *   <li>{@code NONE}     — never indexed</li>
 *   <li>{@code INDEXING} — background worker is chunking + embedding</li>
 *   <li>{@code READY}    — vector store populated, chat is available</li>
 *   <li>{@code FAILED}   — indexing hit an unrecoverable error</li>
 * </ul>
 */
public enum IndexStatus {
    NONE,
    INDEXING,
    READY,
    FAILED
}
