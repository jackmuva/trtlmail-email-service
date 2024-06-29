package com.jackmu.scemail.repository;

import com.jackmu.scemail.model.EntryEmailDTO;
import com.jackmu.scemail.model.FinishedSeriesCountsDTO;
import com.jackmu.scemail.model.Subscription;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {

    @Modifying
    @Transactional
    @Query(value = "UPDATE Subscription SET article_num = article_num + 1 " +
            "WHERE Subscription.series_id = :seriesId AND Subscription.article_num = :articleNum",
            nativeQuery = true)
    void incrementArticleNum(@Param("articleNum") Integer articleNum,
                             @Param("seriesId") Long seriesId);

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM subscription USING series " +
            "WHERE subscription.series_id = series.series_id " +
            "AND article_num > series.num_entries", nativeQuery = true)
    void deleteFinishedSubscriptions();

    @Query(value = "SELECT finished_subs.series_id AS seriesId, COUNT(*) AS numFinishedSeries FROM (" +
            "SELECT Subscription.series_id " +
            "FROM Subscription " +
            "LEFT JOIN Series ON Subscription.series_id = Series.series_id " +
            "WHERE Subscription.article_num > Series.num_entries) AS finished_subs " +
            "GROUP BY finished_subs.series_id",
        nativeQuery = true)
    List<FinishedSeriesCountsDTO> findFinishedCounts();

    @Query(value = "select distinct Entry.title AS entryTitle, Entry.entry_html AS entryText, Series.title AS seriesTitle, " +
            "Subscription.series_id AS seriesId, Subscription.article_num AS articleNum " +
            "FROM Subscription LEFT JOIN Entry ON Entry.series_Id = Subscription.series_Id AND Subscription.article_num = entry.order_num " +
            "LEFT join Series ON Series.series_Id = Subscription.series_Id " +
            "WHERE Subscription.send_Date = CURRENT_DATE ", nativeQuery = true)
    List<EntryEmailDTO> findEmailsBySendDate();

    List<Subscription> findAllByArticleNumAndSeriesId(Integer articleNum, Long seriesId);

    @Modifying
    @Transactional
    @Query(value = "UPDATE Subscription SET send_date = (send_date + (Series.cadence * INTERVAL '1 day')) FROM Series " +
            "WHERE Subscription.series_id = Series.series_id AND Subscription.send_date = CURRENT_DATE " +
            "AND Subscription.article_num = :articleNum AND Subscription.series_id = :seriesId", nativeQuery = true)
    void updateSendDate(@Param("articleNum") Integer articleNum,
                        @Param("seriesId") Long seriesId);
}