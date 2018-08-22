package com.woowahan.smell.bazzangee.web.api;

import com.woowahan.smell.bazzangee.dto.ReviewResponseDto;
import com.woowahan.smell.bazzangee.repository.ReviewRepository;
import com.woowahan.smell.bazzangee.web.HtmlFormDataBuilder;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.MultiValueMap;
import support.test.AcceptanceTest;

import static org.assertj.core.api.Assertions.assertThat;

public class ApiReviewControllerTest extends AcceptanceTest {
    private static final Logger log = LoggerFactory.getLogger(ApiReviewControllerTest.class);

    @Autowired
    private ReviewRepository reviewRepository;

    @Test
    public void 리뷰_생성() {
        HttpEntity<MultiValueMap<String, Object>> requestCreate = HtmlFormDataBuilder
                .multipartFormData()
                .addParameter("contents", "컨텐츠입니다.123")
                .addParameter("image", new ClassPathResource("static/img/baezzangee.png"))
                .addParameter("starPoint", 4.5)
                .build();
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity("/api/reviews", requestCreate, Void.class);
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    }

    @Test
    public void 리뷰_삭제() {
        HttpEntity<MultiValueMap<String, Object>> requestCreate = HtmlFormDataBuilder
                .multipartFormData()
                .addParameter("contents", "컨텐츠입니다.123")
                .addParameter("image", new ClassPathResource("static/img/baezzangee.png"))
                .addParameter("starPoint", 4.5)
                .build();
        ResponseEntity<Void> response = basicAuthTemplate().postForEntity("/api/reviews", requestCreate, Void.class);
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
        ResponseEntity<Void> response1 = basicAuthTemplate().exchange(
                "/api/reviews/1",
                HttpMethod.DELETE,
                new HttpEntity<>(new HttpHeaders()),
                Void.class);
        assertThat(response1.getStatusCode()).isEqualTo(HttpStatus.OK);
    }

    @Test
    public void 리뷰_수정() {
        HttpEntity<MultiValueMap<String, Object>> requestCreate = HtmlFormDataBuilder
                .multipartFormData()
                .addParameter("contents", "컨텐츠입니다.123")
                .addParameter("image", new ClassPathResource("static/img/baezzangee.png"))
                .addParameter("starPoint", 4.5)
                .build();
        ResponseEntity<Void> response = basicAuthTemplate().exchange("/api/reviews/1", HttpMethod.PUT, requestCreate, Void.class);
        assertThat(HttpStatus.OK).isEqualTo(response.getStatusCode());
    }

    @Test
    public void 리뷰_좋아요() {
        ResponseEntity<ReviewResponseDto> response = basicAuthTemplate().getForEntity(String.format("/api/reviews/%d/good", 1), ReviewResponseDto.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("리뷰 좋아요 : {}", reviewRepository.findById(1L).get());
    }

    @Test
    public void 리뷰_좋아요_재시도() {
        log.debug("리뷰 좋아요1 : {}", reviewRepository.findById(1L).get());
        ResponseEntity<ReviewResponseDto> firstResponse = basicAuthTemplate().getForEntity(String.format("/api/reviews/%d/good", 1), ReviewResponseDto.class);
        log.debug("리뷰 좋아요2 : {}", reviewRepository.findById(1L).get());
        ResponseEntity<ReviewResponseDto> secondResponse = basicAuthTemplate().getForEntity(String.format("/api/reviews/%d/good", 1), ReviewResponseDto.class);
        assertThat(secondResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        log.debug("리뷰 좋아요3 : {}", reviewRepository.findById(1L).get());
    }
}