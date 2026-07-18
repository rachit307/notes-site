package com.rachit.notessite;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;

@Controller
public class NotesController {

    private final NotesService notesService;

    public NotesController(NotesService notesService) {
        this.notesService = notesService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("categories", notesService.getCategoryTree());
        return "index";
    }

    @GetMapping("/notes/{category}/{slug}")
    public String viewNote(@PathVariable String category, @PathVariable String slug, Model model) {
        NotesService.RenderedNote note = notesService.renderNote(category, slug);
        if (note == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Note not found: " + category + "/" + slug);
        }
        model.addAttribute("categories", notesService.getCategoryTree());
        model.addAttribute("title", note.title());
        model.addAttribute("contentHtml", note.html());
        return "note";
    }
}
