package com.crescendo.member.util;

import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

public class MultipartFileUtil {

    public static MultipartFile convertBytesToMultipartFile(byte[] bytes,String filename) throws IOException {
        // 바이트 배열을 InputStream으로 변환
        ByteArrayInputStream inputStream = new ByteArrayInputStream(bytes);

        // MultipartFile 객체 생성
        return new ByteArrayMultipartFile(bytes,filename,inputStream);
    }

    private static class ByteArrayMultipartFile implements MultipartFile {

        private final byte[] content;
        private final String filename;
        private final ByteArrayInputStream inputStream;

        public ByteArrayMultipartFile(byte[] content, String filename, ByteArrayInputStream inputStream) {
            this.content = content;
            this.inputStream = inputStream;
            this.filename=filename;
        }

        @Override
        public String getName() {
            return null;
        }

        @Override
        public String getOriginalFilename() {
            return this.filename;
        }

        @Override
        public String getContentType() {
            // 이미지의 MIME 타입을 설정
            // 여기서는 간단하게 이미지 파일의 MIME 타입을 지정합니다.
            return MediaType.IMAGE_JPEG_VALUE; // 또는 MediaType.IMAGE_PNG_VALUE
        }

        @Override
        public boolean isEmpty() {
            return content == null || content.length == 0;
        }

        @Override
        public long getSize() {
            return content.length;
        }

        @Override
        public byte[] getBytes() throws IOException {
            return content;
        }

        @Override
        public InputStream getInputStream() throws IOException {
            return inputStream;
        }

        @Override
        public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
            throw new UnsupportedOperationException("TransferTo() operation is not supported for ByteArrayMultipartFile");
        }
    }
}