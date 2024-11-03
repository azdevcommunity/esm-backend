package com.example.medrese.Service;


import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ChannelListResponse;

import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Component;

import java.math.BigInteger;

@Component
@FieldDefaults(makeFinal = true, level = lombok.AccessLevel.PRIVATE)
public class YouTubeStatistic implements IStatistic {

    static YouTube.Channels.List request;
    static ChannelListResponse response;
    static YouTube youtube;

    static {
        youtube = ConnectYoutubeApi.youtubeService;
        try {
            request = youtube.channels().list("statistics");
            response = request.setKey(ConnectYoutubeApi.DEVELOPER_KEY)
                    .setId(ConnectYoutubeApi.CHANNEL_ID)
                    .execute();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    @Override
    public Platform getPlatform() {
        return Platform.YOUTUBE;
    }

    @Override
    public BigInteger getSubscriberCount() {
        return response.getItems().get(0).getStatistics().getSubscriberCount();
    }

    @Override
    public BigInteger getViewCount() {
        return response.getItems().get(0).getStatistics().getViewCount();
    }

    @Override
    public BigInteger getVideoCount() {
        return response.getItems().get(0).getStatistics().getVideoCount();
    }



}
