package com.example.medrese.Model;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.time.Instant;

@Entity
@Table(name = "channel_stat")
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChannelStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Integer id;

    @Column(name = "view_count")
    Long viewCount;   // C# ulong? → Java Long (nullable)

    @Column(name = "subscriber_count")
    Long subscriberCount;

    @Column(name = "video_count")
    Long videoCount;

    @Column(name = "hidden_subscriber_count")
    Boolean hiddenSubscriberCount;

    @Column(name = "captured_at_utc", nullable = false, updatable = false)
    Instant capturedAtUtc = Instant.now();
}
