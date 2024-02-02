package com.crescendo.config;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpInputMessage;
import org.springframework.http.HttpOutputMessage;
import org.springframework.http.MediaType;
import org.springframework.http.converter.AbstractHttpMessageConverter;
import org.springframework.http.converter.HttpMessageNotReadableException;

import java.io.IOException;
import java.util.List;

public class ByteArrayHttpMessageConverter extends AbstractHttpMessageConverter<List<byte[]>> {

    public ByteArrayHttpMessageConverter() {
        super(MediaType.APPLICATION_PDF);
    }

    @Override
    protected boolean supports(Class<?> clazz) {
        return List.class.isAssignableFrom(clazz);
    }

    @Override
    protected List<byte[]> readInternal(Class<? extends List<byte[]>> clazz, HttpInputMessage inputMessage) throws IOException, HttpMessageNotReadableException {
        return null;
    }

    @Override
    protected void writeInternal(List<byte[]> byteArrayList, HttpOutputMessage outputMessage) throws IOException {
        HttpHeaders headers = outputMessage.getHeaders();
        // 콘텐츠 유형 설정
        headers.setContentType(MediaType.APPLICATION_PDF);

        // 필요한 경우 콘텐츠 디스포지션 설정
        headers.setContentDispositionFormData("attachment", "your_filename.pdf");

        // 바이트 배열을 출력 스트림에 작성
        for (byte[] byteArray : byteArrayList) {
            outputMessage.getBody().write(byteArray);
        }
    }

    @Override
    public List<MediaType> getSupportedMediaTypes(Class<?> clazz) {
        return super.getSupportedMediaTypes(clazz);
    }
}
