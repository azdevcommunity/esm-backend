package com.example.medrese.Repository;

import com.example.medrese.Model.ChannelStat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface ChannelStatRepository extends JpaRepository<ChannelStat, Integer> {
    
    @Query("SELECT c FROM ChannelStat c ORDER BY c.capturedAtUtc DESC LIMIT 1")
    Optional<ChannelStat> findLatestChannelStat();
}
