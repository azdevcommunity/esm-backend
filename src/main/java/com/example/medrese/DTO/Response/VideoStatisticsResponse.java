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
    
    long videoCount;        // isShort=false olan videoların sayısı
    long playlistCount;     // playlist sayısı
    long shortVideoCount;   // isShort=true olan videoların sayısı
    long viewCount;         // statik 100k
}