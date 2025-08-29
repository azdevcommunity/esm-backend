package com.example.medrese.DTO.Response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class VideoStatisticsResponse {
    
    long videoCount;        // ChannelStat'dan video sayısı
    long viewCount;         // ChannelStat'dan view count
    long playlistCount;     // playlist sayısı
    long subscriberCount;   // ChannelStat'dan subscriber count
}