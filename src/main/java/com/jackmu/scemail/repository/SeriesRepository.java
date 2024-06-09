package com.jackmu.scemail.repository;

import com.jackmu.scemail.model.Series;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE Series SET num_current_readers = num_current_readers - :numReaders " +
            "WHERE series_id = :seriesId",
            nativeQuery = true)
    void decrementCurrentReaders(@Param("seriesId") Long seriesId,
                                 @Param("numReaders") Integer numReaders);
}
