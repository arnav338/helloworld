package dev.learning.rag.core;

import dev.learning.rag.model.DocumentPage;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ParagraphChunkerTest {
    /** Protects two public promises: long input is split and citation metadata survives. */
    @Test
    void preservesPageAndCreatesMultipleChunks() {
        String text = "first paragraph ".repeat(20) + "\n\n" + "second paragraph ".repeat(20);
        var chunks = new ParagraphChunker(180, 30).chunk(UUID.randomUUID(), "guide.pdf", List.of(new DocumentPage(7, text)));
        assertTrue(chunks.size() > 1);
        assertTrue(chunks.stream().allMatch(chunk -> chunk.pageNumber() == 7));
        assertEquals("guide.pdf", chunks.getFirst().filename());
    }

    /** Blank pages must not cause empty chunks/model calls/database rows. */
    @Test
    void skipsBlankPages() {
        assertTrue(new ParagraphChunker(100, 10).chunk(UUID.randomUUID(), "x.pdf", List.of(new DocumentPage(1, "  "))).isEmpty());
    }
}
