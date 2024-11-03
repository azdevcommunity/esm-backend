package com.example.medrese.Core.DTO.Request.Update;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.apache.commons.lang3.builder.ToStringExclude;

import java.util.Set;

@Data
@AllArgsConstructor
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UpdateQuestion {

    String question;

    String answer;

    @ToStringExclude
    @EqualsAndHashCode.Exclude
    Set<Integer> categories;

}
