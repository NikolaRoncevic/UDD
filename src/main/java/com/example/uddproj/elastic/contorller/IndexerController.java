package com.example.uddproj.elastic.contorller;

import com.example.uddproj.elastic.dto.IndexBookDTO;
import com.example.uddproj.elastic.dto.IndexReviewerDTO;
import com.example.uddproj.elastic.model.BookDoc;
import com.example.uddproj.elastic.model.ReviewerDoc;
import com.example.uddproj.elastic.model.WriterName;
import com.example.uddproj.elastic.service.BookDocService;
import com.example.uddproj.elastic.service.ReviewerDocService;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@RestController
public class IndexerController {
    @Autowired
    private BookDocService bookDocService;

    @Autowired
    private ReviewerDocService reviewerDocService;


    @RequestMapping(method = RequestMethod.POST, value="index/book",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> indexBook(@RequestBody IndexBookDTO indexBookDTO) throws IOException {
        System.out.println("PRvi put" +  indexBookDTO.getFileName());
        String bookContent = bookDocService.parsePdf(indexBookDTO.getFileName());

        BookDoc bookDoc = new BookDoc();
        bookDoc.setFileName(indexBookDTO.getFileName());
        bookDoc.setBookContent(bookContent);
        bookDoc.setBookGenre(indexBookDTO.getGenre());
        bookDoc.setWriters(new ArrayList<>());
        bookDoc.setIsOpenAccess(indexBookDTO.getIsOpenAccess());
        bookDoc.setBookTitle(indexBookDTO.getTitle());
        bookDoc.getWriters().add(new WriterName(indexBookDTO.getWriters().get(0)));

        bookDocService.bookDocIndexer(bookDoc);

        return new ResponseEntity<>(null, HttpStatus.OK);
    }

    @RequestMapping(method = RequestMethod.POST,value = "index/reviewer",consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<?> indexReviewer(@RequestBody IndexReviewerDTO indexReviewerDTO) throws IOException {
        ReviewerDoc reviewerDoc = new ReviewerDoc();

        reviewerDoc.setReviewerFirstName(indexReviewerDTO.getReviewerFirstName());
        reviewerDoc.setReviewerLastName(indexReviewerDTO.getReviewerLastName());
        reviewerDoc.setReviewerUsername(indexReviewerDTO.getReviewerUsername());
        reviewerDoc.setReviewerLocation(new GeoPoint(indexReviewerDTO.getLat(),indexReviewerDTO.getLng()));

        reviewerDocService.reviewerDocIndexer(reviewerDoc);
        return new ResponseEntity<>(null,HttpStatus.OK);
    }
}
