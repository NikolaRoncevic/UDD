package com.example.uddproj.elastic.service;

import com.example.uddproj.elastic.model.BookDoc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDDocumentInformation;
import org.apache.pdfbox.text.PDFTextStripper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Map;

@Service
public class BookDocService {

    private static Path directoryLocation = Paths.get("src/main/resources/books/");
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient client;

    public String bookDocIndexer(BookDoc bookDoc) throws IOException {

        Map<String, Object> documentMapper = objectMapper.convertValue(bookDoc, Map.class);

        IndexRequest indexRequest = new IndexRequest("book-index").id(bookDoc.getFileName()).source(documentMapper);

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

        return indexResponse.getResult().name();
    }

    public String parsePdf(String fileName) {


        File myFile = directoryLocation.resolve(fileName).toFile();
        System.out.println(myFile.getPath());
        String content = "";
        try {
            PDDocument document = PDDocument.load(myFile);

            PDFTextStripper textStripper = new PDFTextStripper();
            textStripper.setStartPage(1);
            textStripper.setEndPage(document.getNumberOfPages());
            if (!document.isEncrypted()) {
                content = textStripper.getText(document);
            }

            document.close();
        }
         catch (IOException e) {
            System.out.println("Exception in parsing pdf document");
            e.printStackTrace();
            return "false";
        }

        return content;
    }
}
