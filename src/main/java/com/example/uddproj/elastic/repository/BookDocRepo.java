package com.example.uddproj.elastic.repository;

import com.example.uddproj.elastic.model.BookDoc;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookDocRepo extends ElasticsearchRepository<BookDoc, String> {

}
