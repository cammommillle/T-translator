package com.example.demo.controller;

import com.example.demo.model.TranslationRequest;
import com.example.demo.repository.TranslationRequestRepository;
import com.example.demo.service.TranslationService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.concurrent.ExecutionException;

@RestController
public class TextController {

    @Autowired
    private TranslationService translationService;

    @Autowired
    private TranslationRequestRepository translationRequestRepository;

    @PostMapping("/submit")
    public String submitText(@RequestParam String text, @RequestParam String langFrom, @RequestParam String langTo, HttpServletRequest request) throws InterruptedException, ExecutionException {

        if (langFrom.equals(langTo)) {
            return text;
        }

        String translatedText;
        try {
            translatedText = translationService.translate(langFrom, langTo, text);
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return "Error occurred during translation.";
        }

        TranslationRequest translationRequest = new TranslationRequest();
        translationRequest.setIpAddress(request.getRemoteAddr());
        translationRequest.setInputText(text);
        translationRequest.setTranslatedText(translatedText);
        translationRequest.setLangFrom(langFrom);
        translationRequest.setLangTo(langTo);

        translationRequestRepository.save(translationRequest);

        return translatedText;
    }


}
