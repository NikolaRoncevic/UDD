package com.example.uddproj.elastic.service;

import com.example.uddproj.elastic.model.BookDoc;
import com.example.uddproj.elastic.model.ReviewerDoc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.elasticsearch.action.index.IndexRequest;
import org.elasticsearch.action.index.IndexResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.Map;

@Service
public class ReviewerDocService {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private RestHighLevelClient client;

    public String reviewerDocIndexer(ReviewerDoc reviewerDoc) throws IOException {

        Map<String, Object> documentMapper = objectMapper.convertValue(reviewerDoc, Map.class);

        documentMapper.put("reviewerLocation", reviewerDoc.getReviewerLocation().getLat() + "," + reviewerDoc.getReviewerLocation().getLon());

        IndexRequest indexRequest = new IndexRequest("reviewer-index").id(reviewerDoc.getReviewerUsername()).source(documentMapper);

        IndexResponse indexResponse = client.index(indexRequest, RequestOptions.DEFAULT);

        return indexResponse.getResult().name();
    }
}
