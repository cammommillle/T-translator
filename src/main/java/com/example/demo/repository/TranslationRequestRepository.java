package com.example.demo.repository;

import com.example.demo.model.TranslationRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TranslationRequestRepository {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    public void save(TranslationRequest request) {
        String sql = "INSERT INTO translation_requests (ip_address, input_text, translated_text, lang_from, lang_to) VALUES (?, ?, ?, ?, ?)";
        jdbcTemplate.update(sql, request.getIpAddress(), request.getInputText(), request.getTranslatedText(), request.getLangFrom(), request.getLangTo());
    }
}
