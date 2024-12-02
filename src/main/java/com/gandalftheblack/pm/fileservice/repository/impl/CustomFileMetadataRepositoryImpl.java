package com.gandalftheblack.pm.fileservice.repository.impl;

import com.gandalftheblack.pm.fileservice.model.entity.FileMetadataEntity;
import com.gandalftheblack.pm.fileservice.model.entity.FileStatus;
import com.gandalftheblack.pm.fileservice.repository.CustomFileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.aggregation.MatchOperation;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomFileMetadataRepositoryImpl implements CustomFileMetadataRepository {

    private final MongoTemplate mongoTemplate;
    private final static String COLLECTION_NAME = "fileMetadata";

    @Override
    public List<FileMetadataEntity> findAllByOwnerFilteredByStatusAndName(String owner, List<FileStatus> status, String nameFilter) {
        Criteria matchCriteria = new Criteria("owner").is(owner).and("status").in(status);
        if (StringUtils.isNotBlank(nameFilter)) {
            matchCriteria.and("fileName").regex(nameFilter, "i");
        }
        MatchOperation matchOperation = Aggregation.match(matchCriteria);
        Aggregation aggregation = Aggregation.newAggregation(matchOperation);
        AggregationResults<FileMetadataEntity> results = mongoTemplate.aggregate(aggregation, COLLECTION_NAME,
                FileMetadataEntity.class);
        return results.getMappedResults();
    }
}
