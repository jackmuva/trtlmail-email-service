package com.jackmu.scemail.model;

import javax.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "Series")
public class Series {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "series_id")
    private Long seriesId;

    @Column(name = "datetime")
    private LocalDateTime datetime;

    @Column(name = "num_entries")
    private Integer numEntries;

    @Column(name = "title")
    private String title;

    @Column(name = "summary", columnDefinition="TEXT")
    private String summary;

    @Column(name = "tags")
    private String tags;

    @Column(name = "cadence")
    private Integer cadence;

    @Column(name = "pen_name")
    private String penName;

    @Column(name = "email")
    private String email;

    @Column(name = "published")
    private Boolean published;

    @Column(name = "num_all_time_readers")
    private Integer numAllTimeReaders;

    @Column(name = "numCurrentReaders")
    private Integer numCurrentReaders;
}
