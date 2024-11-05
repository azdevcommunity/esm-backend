package com.example.medrese.Controller;


import com.example.medrese.DTO.Request.Create.CreateStatisticDTO;
import com.example.medrese.DTO.Response.StatisticResponse;
import com.example.medrese.Model.Statistic;
import com.example.medrese.Service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.ldap.StartTlsResponse;
import java.util.List;
import java.util.Optional;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/statistics")
public class StatisticsController {
    @Autowired
    private StatisticsService statisticService;

    @GetMapping
    public List<Statistic> getAllStatistics() {
        return statisticService.getAllStatistics();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Statistic> getStatisticById(@PathVariable Integer id) {
        return ResponseEntity.ok(statisticService.getStatisticById(id));
    }

    @PostMapping
    public ResponseEntity<StatisticResponse> createStatistic(@RequestBody CreateStatisticDTO createStatisticDTO) {
        return ResponseEntity.ok(statisticService.createStatistic(createStatisticDTO)) ;
    }

    @PutMapping("/{id}")
    public ResponseEntity<Statistic> updateStatistic(@PathVariable Integer id, @RequestBody Statistic statisticDetails) {
        try {
            Statistic updatedStatistic = statisticService.updateStatistic(id, statisticDetails);
            return ResponseEntity.ok(updatedStatistic);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStatistic(@PathVariable Integer id) {
        statisticService.deleteStatistic(id);
        return ResponseEntity.noContent().build();
    }
}

