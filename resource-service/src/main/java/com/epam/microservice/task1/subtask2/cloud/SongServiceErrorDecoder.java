package com.epam.microservice.task1.subtask2.cloud;

import com.epam.microservice.task1.subtask2.controller.dto.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Slf4j
@Component
public class SongServiceErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        try {
            SongServiceException ex = new SongServiceException("Song service error");
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorMessage message = objectMapper.readValue(response.body().asReader(Charset.defaultCharset()), ErrorMessage.class);
            ex.setErrorMessage(message);
            return ex;
        } catch (Exception e) {
            log.error("Song service error", e);
        }
        return new SongServiceException("Song service error");
    }
}
