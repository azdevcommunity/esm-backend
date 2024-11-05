package com.example.medrese.Service;


import com.example.medrese.DTO.Request.Create.CreateStatisticDTO;
import com.example.medrese.DTO.Response.StatisticResponse;
import com.example.medrese.Model.Statistic;
import com.example.medrese.Repository.StatisticRepository;
import com.example.medrese.mapper.StatisticMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)

public class StatisticsService  {
     StatisticRepository statisticRepository;
     StatisticMapper statisticMapper;

    public List<Statistic> getAllStatistics() {
        return statisticRepository.findAll();
    }

    public Statistic getStatisticById(Integer id) {
        return statisticRepository.findById(id).orElseThrow(()->new RuntimeException("statistics not found"));
    }

    public StatisticResponse createStatistic(CreateStatisticDTO createStatisticDTO) {
        Statistic statistic = statisticMapper.toEntity(createStatisticDTO);
        statistic = statisticRepository.save(statistic);
        return statisticMapper.toResponse(statistic);


    }

    public Statistic updateStatistic(Integer id, Statistic statisticDetails) {
        return statisticRepository.findById(id).map(statistic -> {
            statistic.setPlatformName(statisticDetails.getPlatformName());
            statistic.setViewCount(statisticDetails.getViewCount());
            statistic.setSubscriberCount(statisticDetails.getSubscriberCount());
            statistic.setVideoCount(statisticDetails.getVideoCount());
            return statisticRepository.save(statistic);
        }).orElseThrow(() -> new RuntimeException("Statistic not found with id " + id));
    }

    public void deleteStatistic(Integer id) {
        if (!statisticRepository.existsById(id)){
            throw new RuntimeException("does not exist");
        }
        statisticRepository.deleteById(id);
    }
}
