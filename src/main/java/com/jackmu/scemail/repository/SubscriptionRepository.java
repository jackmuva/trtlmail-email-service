package com.jackmu.scemail.repository;

import com.jackmu.scemail.model.EntryEmailDTO;
import com.jackmu.scemail.model.Subscription;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SubscriptionRepository extends JpaRepository<Subscription, Long> {
    @Modifying
    @Transactional
    void deleteBySubscriberEmailAndSeriesId(String email, Long seriesId);

    @Modifying
    @Transactional
    @Query("UPDATE Subscription SET articleNum = articleNum + 1")
    void incrementArticleNum();

    @Modifying
    @Transactional
    @Query(value = "DELETE FROM subscription USING series " +
            "WHERE subscription.series_id = series.series_id " +
            "AND article_num >= series.num_entries", nativeQuery = true)
    void deleteFinishedSubscriptions();

    @Query(value = "select Entry.title AS entryTitle, Entry.entry_html AS entryText, Subscription.subscriber_Email AS subscriberEmail, Series.title AS seriesTitle " +
            "FROM Subscription LEFT JOIN Entry ON Entry.series_Id = Subscription.series_Id AND Subscription.article_num = entry.order_num " +
            "LEFT join Series ON Series.series_Id = Subscription.series_Id " +
            "WHERE Subscription.send_Date = CURRENT_DATE", nativeQuery = true)
    List<EntryEmailDTO> findEmailsBySendDate();

    @Modifying
    @Transactional
    @Query(value = "UPDATE Subscription SET send_date = (send_date + (Series.cadence * INTERVAL '1 day')) FROM Series " +
            "WHERE Subscription.series_id = Series.series_id AND Subscription.send_date = CURRENT_DATE", nativeQuery = true)
    void updateSendDate();
}