package com.jackmu.scemail.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Subscription")
public class Subscription {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "subscription_id")
    private Long subscriptionId;

    @Column(name = "subscriber_email")
    private String subscriberEmail;

    @Column(name = "article_num")
    private Integer articleNum;

    @Column(name = "send_date")
    private LocalDate sendDate;

    @Column(name = "series_id")
    private Long seriesId;
}
