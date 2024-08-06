package com.example.demo;

import com.example.demo.service.TranslationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.client.RestTemplate;
import java.util.concurrent.ExecutionException;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class TranslationServiceTest {

    @Mock
    private RestTemplate restTemplate;

    @InjectMocks
    private TranslationService translationService;

    @BeforeEach
    public void setUp() {
    }

    @Test
    public void testTranslate() throws InterruptedException, ExecutionException {
        String langFrom = "en";
        String langTo = "fr";
        String text = "Hello world";
        String translatedWord1 = "Bonjour";
        String translatedWord2 = "monde";

        when(restTemplate.getForObject("https://script.google.com/macros/s/AKfycbye8HmoCku3NPWV6VqL3Vd1BOfkgb5wVRJaGJoIApS_Mg4wHJSvC-09b95-zaWq-arK4Q/exec?q=Hello&target=fr&source=en", String.class))
                .thenReturn(translatedWord1);
        when(restTemplate.getForObject("https://script.google.com/macros/s/AKfycbye8HmoCku3NPWV6VqL3Vd1BOfkgb5wVRJaGJoIApS_Mg4wHJSvC-09b95-zaWq-arK4Q/exec?q=world&target=fr&source=en", String.class))
                .thenReturn(translatedWord2);

        String translatedText = translationService.translate(langFrom, langTo, text);

        assertEquals("Bonjour monde", translatedText);
    }
}


