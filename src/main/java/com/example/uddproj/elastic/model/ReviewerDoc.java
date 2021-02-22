package com.example.uddproj.elastic.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.elasticsearch.common.geo.GeoPoint;
import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;
import org.springframework.data.elasticsearch.annotations.GeoPointField;

@Document(indexName = "reviewer-index")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ReviewerDoc {

    @Id
    @Field(type = FieldType.Keyword, analyzer = "serbian")
    private String reviewerUsername;

    @Field(type = FieldType.Text, analyzer = "serbian")
    private String reviewerFirstName;

    @Field(type = FieldType.Text, analyzer = "serbian")
    private String reviewerLastName;

    @GeoPointField
    private GeoPoint reviewerLocation;

    // Geopint = new Geopint (double lat, double long)
}