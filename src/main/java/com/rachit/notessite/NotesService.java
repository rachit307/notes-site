package com.rachit.notessite;

import com.vladsch.flexmark.html.HtmlRenderer;
import com.vladsch.flexmark.parser.Parser;
import com.vladsch.flexmark.util.ast.Node;
import com.vladsch.flexmark.util.data.MutableDataSet;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Scans a folder-per-category, file-per-note structure on disk and exposes it
 * as a navigable tree, plus renders individual notes from markdown to HTML.
 *
 * Expected layout (configurable via notes.dir):
 *   notes/
 *     docker/
 *       what-is-docker.md
 *       architecture.md
 *     kubernetes/
 *       pods-and-services.md
 */
@Service
public class NotesService {

    private final Path notesRoot;
    private final Parser markdownParser;
    private final HtmlRenderer htmlRenderer;

    public NotesService(@Value("${notes.dir:notes}") String notesDir) {
        this.notesRoot = Path.of(notesDir).toAbsolutePath().normalize();
        MutableDataSet options = new MutableDataSet();
        this.markdownParser = Parser.builder(options).build();
        this.htmlRenderer = HtmlRenderer.builder(options).build();
    }

    /** category name -> list of notes in that category, alphabetically sorted */
    public Map<String, List<NoteMeta>> getCategoryTree() {
        if (!Files.isDirectory(notesRoot)) {
            return Collections.emptyMap();
        }
        try (Stream<Path> categories = Files.list(notesRoot)) {
            Map<String, List<NoteMeta>> tree = new TreeMap<>();
            for (Path categoryDir : categories.filter(Files::isDirectory).collect(Collectors.toList())) {
                String category = categoryDir.getFileName().toString();
                List<NoteMeta> notes = listNotesInCategory(categoryDir, category);
                if (!notes.isEmpty()) {
                    tree.put(category, notes);
                }
            }
            return tree;
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to scan notes directory: " + notesRoot, e);
        }
    }

    private List<NoteMeta> listNotesInCategory(Path categoryDir, String category) throws IOException {
        try (Stream<Path> files = Files.list(categoryDir)) {
            return files
                    .filter(p -> p.getFileName().toString().endsWith(".md"))
                    .map(p -> toNoteMeta(p, category))
                    .sorted(Comparator.comparing(NoteMeta::title))
                    .collect(Collectors.toList());
        }
    }

    private NoteMeta toNoteMeta(Path file, String category) {
        String filename = file.getFileName().toString();
        String slug = filename.substring(0, filename.length() - ".md".length());
        String title = extractTitle(file, slug);
        return new NoteMeta(category, slug, title);
    }

    /** Uses the first "# Heading" line as the title if present, else derives from filename */
    private String extractTitle(Path file, String slug) {
        try {
            List<String> lines = Files.readAllLines(file);
            for (String line : lines) {
                if (line.startsWith("# ")) {
                    return line.substring(2).trim();
                }
            }
        } catch (IOException ignored) {
            // fall through to filename-derived title
        }
        String spaced = slug.replace('-', ' ').replace('_', ' ');
        return Character.toUpperCase(spaced.charAt(0)) + spaced.substring(1);
    }

    /** Renders one note's markdown content to HTML. Returns null if not found. */
    public RenderedNote renderNote(String category, String slug) {
        Path file = notesRoot.resolve(category).resolve(slug + ".md");
        if (!Files.isRegularFile(file)) {
            return null;
        }
        try {
            String markdown = Files.readString(file);
            Node document = markdownParser.parse(markdown);
            String html = htmlRenderer.render(document);
            String title = extractTitle(file, slug);
            return new RenderedNote(title, html);
        } catch (IOException e) {
            throw new UncheckedIOException("Failed to read note: " + file, e);
        }
    }

    public record RenderedNote(String title, String html) {
    }
}
