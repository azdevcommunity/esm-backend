package com.example.medrese.Controller;


import com.example.medrese.Model.Statistic;
import com.example.medrese.Service.StatisticsService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
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
        Optional<Statistic> statistic = statisticService.getStatisticById(id);
        return statistic.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Statistic createStatistic(@RequestBody Statistic statistic) {
        return statisticService.createStatistic(statistic);
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

