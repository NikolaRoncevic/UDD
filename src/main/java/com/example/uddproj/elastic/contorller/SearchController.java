package com.example.uddproj.elastic.contorller;

import com.example.uddproj.elastic.service.SearchService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class SearchController {

    @Autowired
    private SearchService searchService;

    @GetMapping("/search")
    public ResponseEntity<?> search(@RequestParam(value = "bookTitle", required = false, defaultValue = "") String bookTitle,
                                    @RequestParam(value = "bookTitleRequired", required = false, defaultValue = "false") boolean bookTitleRequired,
                                    @RequestParam(value = "bookTitlePhrase", required = false, defaultValue = "false") boolean bookTitlePhrase,
                                    @RequestParam(value = "writerFullName", required = false, defaultValue = "") String writerFullName,
                                    @RequestParam(value = "writerFullNameRequired", required = false, defaultValue = "false") boolean writerFullNameRequired,
                                    @RequestParam(value = "bookContent", required = false, defaultValue = "") String bookContent,
                                    @RequestParam(value = "bookContentRequired", required = false, defaultValue = "false") boolean bookContentRequired,
                                    @RequestParam(value = "bookContentPhrase", required = false, defaultValue = "false") boolean bookContentPhrase,
                                    @RequestParam(value = "bookGenre", required = false, defaultValue = "") String bookGenre,
                                    @RequestParam(value = "bookGenreRequired", required = false, defaultValue = "false") boolean bookGenreRequired) throws Exception {

        return new ResponseEntity<>(searchService.searchBook(bookTitle, bookTitleRequired, bookTitlePhrase,
                writerFullName, writerFullNameRequired,
                bookContent, bookContentRequired, bookContentPhrase,
                bookGenre, bookGenreRequired), HttpStatus.OK);
    }

    @GetMapping("/find-all-books")
    public ResponseEntity<?> findAllBooks() throws Exception {

        return new ResponseEntity<>(searchService.findAllBooks(), HttpStatus.OK);
    }

    @GetMapping("find-all-reviewers")
    public ResponseEntity<?> findAllReviewers() throws Exception {
        return new ResponseEntity<>(searchService.findAllReviewers(),HttpStatus.OK);

    }
}
