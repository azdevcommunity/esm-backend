package com.example.medrese.Config;

import jakarta.validation.Validation;
import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import javax.xml.validation.Validator;


@Configuration
@Lazy
public class Configurations {
    @Bean
    public ModelMapper getMapper() {
        return new ModelMapper();
    }
    @Bean
    public Validator validator() {
     return (Validator) Validation.buildDefaultValidatorFactory().getValidator();
    }

}
