package plottwist.backend.services.indexing;

import java.util.Locale;
import java.util.Set;

import org.springframework.stereotype.Component;

/**
 * Determines whether a file in the repo tree should be indexed
 * based on extension and size limits.
 */
@Component
public class CodeFileFilter {

    /** Extensions we consider "code" and worth embedding. */
    private static final Set<String> ALLOWED_EXTENSIONS = Set.of(
            "java", "kt", "scala", "groovy",
            "js", "jsx", "ts", "tsx", "mjs", "cjs",
            "py", "rb", "go", "rs", "c", "cpp", "h", "hpp", "cs",
            "swift", "m", "mm",
            "php", "lua", "r", "jl",
            "sql", "graphql", "gql",
            "html", "css", "scss", "less", "sass",
            "json", "yaml", "yml", "toml", "xml",
            "md", "mdx", "txt", "rst",
            "sh", "bash", "zsh", "fish", "ps1", "bat", "cmd",
            "dockerfile", "makefile",
            "proto", "thrift", "avdl",
            "tf", "hcl",
            "gradle", "sbt", "cmake"
    );

    /** Paths/directories to always skip. */
    private static final Set<String> SKIP_DIRS = Set.of(
            "node_modules", ".git", ".svn", ".hg",
            "__pycache__", ".gradle", "build", "dist",
            "target", ".next", ".nuxt", "vendor",
            ".idea", ".vscode"
    );

    /**
     * Returns true if the file should be chunked and embedded.
     */
    public boolean isEligible(String path, long sizeBytes, long maxBytes) {
        if (sizeBytes > maxBytes || sizeBytes == 0) return false;

        // Skip ignored directories
        for (String dir : SKIP_DIRS) {
            if (path.contains("/" + dir + "/") || path.startsWith(dir + "/")) {
                return false;
            }
        }

        String ext = extractExtension(path);
        return ALLOWED_EXTENSIONS.contains(ext);
    }

    String extractExtension(String path) {
        String lower = path.toLowerCase(Locale.ROOT);
        String fileName = lower.substring(lower.lastIndexOf('/') + 1);
        if ("dockerfile".equals(fileName)) return "dockerfile";
        if ("makefile".equals(fileName))   return "makefile";

        int dot = fileName.lastIndexOf('.');
        if (dot < 0) return "text";
        return fileName.substring(dot + 1);
    }
}
