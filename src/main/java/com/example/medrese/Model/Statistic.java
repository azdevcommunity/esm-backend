package com.example.medrese.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;
import lombok.experimental.FieldDefaults;

@Data
@Entity
@AllArgsConstructor
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "statistics")
public class Statistic  {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    Integer id;

    @Column(name = "platform_name")
    String platformName;

    @Column(name = "view_count")
    String viewCount;

    @NotNull
    @Column(name = "subscriber_count")
    String subscriberCount;

    @Column(name = "video_count")
    String videoCount;
}
