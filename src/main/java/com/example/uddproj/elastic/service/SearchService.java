package com.example.uddproj.elastic.service;

import com.example.uddproj.elastic.model.BookDoc;
import com.example.uddproj.elastic.model.ReviewerDoc;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.lucene.search.join.ScoreMode;
import org.elasticsearch.action.search.SearchRequest;
import org.elasticsearch.action.search.SearchResponse;
import org.elasticsearch.client.RequestOptions;
import org.elasticsearch.client.RestHighLevelClient;
import org.elasticsearch.common.unit.DistanceUnit;
import org.elasticsearch.index.query.*;
import org.elasticsearch.search.SearchHit;
import org.elasticsearch.search.builder.SearchSourceBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@Service
public class SearchService {

    @Autowired
    private RestHighLevelClient client;

    @Autowired
    private ObjectMapper objectMapper;


    public List<BookDoc> searchBook(String bookTitle, boolean bookTitleReq, boolean bookTitlePhrase, String writerName, boolean writerNameReq, String bookContent, boolean bookContentReq, boolean bookContentPhrase, String bookGenre, boolean bookGenreReq) throws IOException {

        SearchRequest searchRequest = new SearchRequest("book-index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        BoolQueryBuilder finalQuery = QueryBuilders.boolQuery();

        appendBookTitleQuery(bookTitle, bookTitleReq, bookTitlePhrase,finalQuery);
        appendBookContentQuery(bookContent, bookContentReq, bookContentPhrase,finalQuery);
        appendWriterNameQuery(writerName,writerNameReq,finalQuery);
        appendBookGenreQuery(bookGenre,bookGenreReq,finalQuery);




        searchSourceBuilder.query(finalQuery);
        searchRequest.source(searchSourceBuilder);
        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        return getBookSearchResult(searchResponse);
    }

    public List<ReviewerDoc> searchReviewers(double longitude, double latitude) throws IOException {

        SearchRequest searchRequest = new SearchRequest("reviewer-index");
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();

        GeoDistanceQueryBuilder myQuery = QueryBuilders.geoDistanceQuery("reviewerLocation").
                point(latitude, longitude).distance(100, DistanceUnit.KILOMETERS);

        // return only reviewers who are NOT in given range
        BoolQueryBuilder finalQuery = QueryBuilders.boolQuery().mustNot(myQuery);

        searchSourceBuilder.query(finalQuery);
        //searchSourceBuilder.query(myQuery);
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        return getReviewerSearchResult(searchResponse);
    }


    public List<BookDoc> findAllBooks() throws Exception {

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        return getBookSearchResult(searchResponse);
    }

    public List<ReviewerDoc> findAllReviewers() throws Exception {

        SearchRequest searchRequest = new SearchRequest();
        SearchSourceBuilder searchSourceBuilder = new SearchSourceBuilder();
        searchSourceBuilder.query(QueryBuilders.matchAllQuery());
        searchRequest.source(searchSourceBuilder);

        SearchResponse searchResponse = client.search(searchRequest, RequestOptions.DEFAULT);

        return getReviewerSearchResult(searchResponse);
    }


    private List<BookDoc> getBookSearchResult(SearchResponse response) {

        SearchHit[] searchHit = response.getHits().getHits();

        List<BookDoc> bookDocuments = new ArrayList<>();

        if (searchHit.length > 0) {

            Arrays.stream(searchHit)
                    .forEach(hit -> bookDocuments
                            .add(objectMapper
                                    .convertValue(hit.getSourceAsMap(),
                                            BookDoc.class))
                    );
        }

        return bookDocuments;
    }

    private List<ReviewerDoc> getReviewerSearchResult(SearchResponse response) {

        SearchHit[] searchHit = response.getHits().getHits();

        List<ReviewerDoc> reviewerDocuments = new ArrayList<>();



        if (searchHit.length > 0) {

            Arrays.stream(searchHit)
                    .forEach(hit -> reviewerDocuments
                            .add(objectMapper
                                    .convertValue(hit.getSourceAsMap(),
                                            ReviewerDoc.class))
                    );

        }

        return reviewerDocuments;
    }

    private void appendBookTitleQuery(String bookTitle,boolean bookTitleReq,boolean bookTitlePhrase,BoolQueryBuilder finalQuery) {

        if(!bookTitle.isEmpty() || !bookTitle.equals("")) {
            if (bookTitleReq) {
                if (bookTitlePhrase) {
                    MatchPhrasePrefixQueryBuilder newQuery = QueryBuilders.matchPhrasePrefixQuery("bookTitle", bookTitle);
                    finalQuery.must(newQuery);
                }
                else {
                    SimpleQueryStringBuilder newQuery = QueryBuilders.simpleQueryStringQuery(bookTitle).field("bookTitle").defaultOperator(Operator.OR);
                    finalQuery.must(newQuery);
                }
            }
            else {
                if (bookTitlePhrase) {
                    MatchPhrasePrefixQueryBuilder newQuery = QueryBuilders.matchPhrasePrefixQuery("bookTitle", bookTitle);
                    finalQuery.should(newQuery);
                }
                else {
                    SimpleQueryStringBuilder newQuery = QueryBuilders.simpleQueryStringQuery(bookTitle).field("bookTitle").defaultOperator(Operator.OR);
                    finalQuery.should(newQuery);
                }

            }
        }
    }

    private void appendBookContentQuery(String bookContent,boolean bookContentReq,boolean bookContentPhrase,BoolQueryBuilder finalQuery) {

        if(!bookContent.isEmpty() || !bookContent.equals("")) {
            if (bookContentReq) {
                if (bookContentPhrase) {
                    MatchPhrasePrefixQueryBuilder newQuery = QueryBuilders.matchPhrasePrefixQuery("bookContent", bookContent);
                    finalQuery.must(newQuery);
                }
                else {
                    SimpleQueryStringBuilder newQuery = QueryBuilders.simpleQueryStringQuery(bookContent).field("bookContent").defaultOperator(Operator.OR);
                    finalQuery.must(newQuery);
                }
            }
            else {
                if (bookContentPhrase) {
                    MatchPhrasePrefixQueryBuilder newQuery = QueryBuilders.matchPhrasePrefixQuery("bookContent", bookContent);
                    finalQuery.should(newQuery);
                }
                else {
                    SimpleQueryStringBuilder newQuery = QueryBuilders.simpleQueryStringQuery(bookContent).field("bookContent").defaultOperator(Operator.OR);
                    finalQuery.should(newQuery);
                }

            }
        }
    }

    private void appendWriterNameQuery(String writerName,boolean writerNameReq, BoolQueryBuilder finalQuery) {
        if(!writerName.isEmpty() || !writerName.equals("")) {
            SimpleQueryStringBuilder tempQuery = QueryBuilders.simpleQueryStringQuery(writerName).field("writers").defaultOperator(Operator.OR);
            NestedQueryBuilder newQuery = QueryBuilders.nestedQuery("writers", tempQuery, ScoreMode.Avg);
            if(writerNameReq) {
                finalQuery.must(newQuery);
            } else {
                finalQuery.should(newQuery);
            }
        }
    }

    private void appendBookGenreQuery(String bookGenre,boolean bookGenreReq, BoolQueryBuilder finalQuery) {
        if(!bookGenre.isEmpty() || !bookGenre.equals("")) {
            if(bookGenreReq) {
                SimpleQueryStringBuilder newQuery = QueryBuilders.simpleQueryStringQuery(bookGenre).field("writerName").defaultOperator(Operator.OR);
                finalQuery.must(newQuery);
            } else {
                SimpleQueryStringBuilder newQuery = QueryBuilders.simpleQueryStringQuery(bookGenre).field("writerName").defaultOperator(Operator.OR);
                finalQuery.should(newQuery);
            }
        }
    }
}
