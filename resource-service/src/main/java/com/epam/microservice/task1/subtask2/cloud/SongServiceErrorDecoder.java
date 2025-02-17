package com.epam.microservice.task1.subtask2.cloud;

import com.epam.microservice.task1.subtask2.controller.dto.ErrorMessage;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Component;

import java.nio.charset.Charset;

@Component
public class SongServiceErrorDecoder implements ErrorDecoder {
    @Override
    public Exception decode(String s, Response response) {
        try {
            //IOUtils.toString(response.body().asReader(Charset.defaultCharset()));
            SongServiceException ex = new SongServiceException("Song service error");
            ObjectMapper objectMapper = new ObjectMapper();
            ErrorMessage message = objectMapper.readValue(response.body().asReader(Charset.defaultCharset()), ErrorMessage.class);
            ex.setErrorMessage(message);
            return ex;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new SongServiceException("Song service error");
    }
}
