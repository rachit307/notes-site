package com.rachit.notessite;

/**
 * Metadata for a single note, used to build the sidebar navigation.
 */
public record NoteMeta(String category, String slug, String title) {
}
