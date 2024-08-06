package com.example.demo.service;

import org.apache.commons.lang3.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
public class TranslationService {

    @Autowired
    private RestTemplate restTemplate;

    private final ExecutorService executorService = Executors.newFixedThreadPool(10);

    public String translate(String langFrom, String langTo, String text) throws InterruptedException, ExecutionException {
        String[] words = text.split("\\s+");
        List<Future<String>> futures = new ArrayList<>();

        for (String word : words) {
            Callable<String> task = () -> StringEscapeUtils.unescapeHtml4(translateWord(langFrom, langTo, word));
            Future<String> future = executorService.submit(task);
            futures.add(future);
        }

        StringBuilder translatedText = new StringBuilder();
        for (Future<String> future : futures) {
            translatedText.append(future.get()).append(" ");
        }

        return capitalizeSentences(translatedText.toString().trim());
    }

    private String translateWord(String langFrom, String langTo, String word) {
        String url = "https://script.google.com/macros/s/AKfycbye8HmoCku3NPWV6VqL3Vd1BOfkgb5wVRJaGJoIApS_Mg4wHJSvC-09b95-zaWq-arK4Q/exec" +
                "?q=" + word +
                "&target=" + langTo +
                "&source=" + langFrom;

        return restTemplate.getForObject(url, String.class);
    }

    private String capitalizeSentences(String text) {
        String[] words = text.split("\\s+");
        StringBuilder capitalizedText = new StringBuilder();
        boolean capitalizeNext = true;

        for (String word : words) {
            if (capitalizeNext) {
                capitalizedText.append(Character.toUpperCase(word.charAt(0)))
                        .append(word.substring(1).toLowerCase());
                capitalizeNext = false;
            } else {
                capitalizedText.append(word.toLowerCase());
            }
            capitalizedText.append(" ");

            if (word.endsWith(".") || word.endsWith("!") || word.endsWith("?")) {
                capitalizeNext = true;
            }
        }

        String result = capitalizedText.toString().trim();
        return result.replaceAll("\\bi\\b", "I");
    }
}
