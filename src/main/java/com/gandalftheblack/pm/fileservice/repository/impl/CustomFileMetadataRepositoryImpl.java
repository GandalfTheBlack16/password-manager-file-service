package com.gandalftheblack.pm.fileservice.repository.impl;

import com.gandalftheblack.pm.fileservice.model.entity.FileMetadataEntity;
import com.gandalftheblack.pm.fileservice.model.entity.value.FileStatus;
import com.gandalftheblack.pm.fileservice.model.response.FileGetResponse;
import com.gandalftheblack.pm.fileservice.repository.CustomFileMetadataRepository;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.*;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CustomFileMetadataRepositoryImpl implements CustomFileMetadataRepository {

    private final MongoTemplate mongoTemplate;
    private final static String COLLECTION_NAME = "fileMetadata";

    @Override
    public Page<FileGetResponse> findAllByOwnerFilteredByStatusAndName(String owner, List<FileStatus> status, String nameFilter, Pageable pageable) {
        Criteria matchCriteria = new Criteria("owner").is(owner).and("status").in(status);
        if (StringUtils.isNotBlank(nameFilter)) {
            matchCriteria.and("fileName").regex(nameFilter, "i");
        }
        MatchOperation matchOperation = Aggregation.match(matchCriteria);
        SortOperation sortOperation = Aggregation.sort(pageable.getSort());
        SkipOperation skipOperation = Aggregation.skip((long) pageable.getPageNumber() * pageable.getPageSize());
        LimitOperation limitOperation = Aggregation.limit(pageable.getPageSize());
        Aggregation aggregation = Aggregation.newAggregation(matchOperation, sortOperation, skipOperation, limitOperation);
        List<FileGetResponse> results = mongoTemplate.aggregate(aggregation, COLLECTION_NAME,
                FileGetResponse.class).getMappedResults();
        long totalResults = mongoTemplate.count(Query.query(matchCriteria), FileMetadataEntity.class);

        return new PageImpl<>(results, pageable, totalResults);
    }
}
