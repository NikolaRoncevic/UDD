package com.example.uddproj.elastic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import java.util.List;

@Document(indexName = "book-index")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class BookDoc {

    @Id
    @Field(type = FieldType.Keyword)
    private String fileName;

    @Field(type = FieldType.Text, analyzer = "serbian")
    private String bookTitle;

    @Field(type = FieldType.Text, analyzer = "serbian")
    private String bookContent;

    @Field(type = FieldType.Text, analyzer = "serbian")
    private String bookGenre;

    @Field(type = FieldType.Nested, analyzer = "serbian")
    private List<WriterName> writers;

    @Field(type = FieldType.Boolean)
    private Boolean isOpenAccess;
}