package com.example.demo;

import com.example.demo.controller.TextController;
import com.example.demo.model.TranslationRequest;
import com.example.demo.repository.TranslationRequestRepository;
import com.example.demo.service.TranslationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
public class TextControllerTest {

    @Mock
    private TranslationService translationService;

    @Mock
    private TranslationRequestRepository translationRequestRepository;

    @InjectMocks
    private TextController textController;

    private MockMvc mockMvc;

    @Test
    public void testSubmitText() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(textController).build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");

        String text = "Hello";
        String langFrom = "en";
        String langTo = "fr";
        String translatedText = "Bonjour";

        when(translationService.translate(langFrom, langTo, text)).thenReturn(translatedText);

        mockMvc.perform(post("/submit")
                        .param("text", text)
                        .param("langFrom", langFrom)
                        .param("langTo", langTo))
                .andExpect(status().isOk())
                .andExpect(content().string(translatedText));

        verify(translationService, times(1)).translate(langFrom, langTo, text);
        verify(translationRequestRepository, times(1)).save(any(TranslationRequest.class));
    }

    @Test
    public void testSubmitTextSameLang() throws Exception {
        mockMvc = MockMvcBuilders.standaloneSetup(textController).build();

        MockHttpServletRequest request = new MockHttpServletRequest();
        request.setRemoteAddr("127.0.0.1");

        String text = "Hello";
        String langFrom = "en";
        String langTo = "en";

        mockMvc.perform(post("/submit")
                        .param("text", text)
                        .param("langFrom", langFrom)
                        .param("langTo", langTo))
                .andExpect(status().isOk())
                .andExpect(content().string(text));

        verify(translationService, times(0)).translate(anyString(), anyString(), anyString());
        verify(translationRequestRepository, times(0)).save(any(TranslationRequest.class));
    }
}

