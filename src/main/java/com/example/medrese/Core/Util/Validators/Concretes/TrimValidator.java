//package com.example.medrese.Core.Util.Validators.Concretes;
//
//import jakarta.persistence.AttributeConverter;
//import org.springframework.stereotype.Service;
//
//@Service
//public class TrimValidator implements AttributeConverter<String, String> {
//    @Override
//    public String convertToDatabaseColumn(String s) {
//        if (s == null) {
//            return null;
//        }
//        return StringEscapeUtils.unescapeJson(StringEscapeUtils.unescapeJava(s.trim())).replaceAll("\\r\\n|\\r|\\n", "\n");
//    }
//
//    @Override
//    public String convertToEntityAttribute(String s) {
//        return s;
//    }
//}
