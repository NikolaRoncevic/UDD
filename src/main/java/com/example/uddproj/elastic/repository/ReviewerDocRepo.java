package com.example.uddproj.elastic.repository;

import com.example.uddproj.elastic.model.ReviewerDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewerDocRepo extends ElasticsearchRepository<ReviewerDoc, String> {

}
