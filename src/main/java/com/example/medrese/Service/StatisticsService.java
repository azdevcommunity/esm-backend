package com.example.medrese.Service;


import com.example.medrese.Model.Statistic;
import com.example.medrese.Repository.StatisticRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StatisticsService  {
    @Autowired
    private StatisticRepository statisticRepository;

    public List<Statistic> getAllStatistics() {
        return statisticRepository.findAll();
    }

    public Optional<Statistic> getStatisticById(Integer id) {
        return statisticRepository.findById(id);
    }

    public Statistic createStatistic(Statistic statistic) {
        return statisticRepository.save(statistic);
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
        statisticRepository.deleteById(id);
    }
}
