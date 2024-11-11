package com.example.medrese.Repository;

import com.example.medrese.Model.Statistic;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;
@Repository
public interface StatisticRepository extends JpaRepository<Statistic, Integer> {
    Optional<Statistic> findByPlatformName (String platformName);
}
