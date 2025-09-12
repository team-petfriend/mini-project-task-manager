package com.example.petfriend.common.enums;

import org.springframework.http.HttpStatus;

public enum ErrorCode {
    BAD_REQUEST(HttpStatus.BAD_REQUEST, "BAD_REQUEST", "요청이 올바르지 않습니다."),
    VALIDATION_ERROR(HttpStatus.BAD_REQUEST, "VALIDATION_ERROR", "입력값 검증에 실패 했습니다."),
    AUTH_INVALID(HttpStatus.UNAUTHORIZED,"UNAUTHORIZED","인증이 필요합니다. 로그인 후 다시 시도해 주십시오"),
    AUTH_FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN","접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "요청하신 자원을 찾을 수 없습니다."),
    USER_DUPLICATED(HttpStatus.CONFLICT, "CONFLICT","이미 가입된 이메일 입니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 오류가 발생했습니다.");

    public final HttpStatus status;
    public final String code;
    public final String defaultMessage;

    ErrorCode(HttpStatus status, String code, String defaultMessage) {
        this.status = status;
        this.code = code;
        this.defaultMessage = defaultMessage;
    }
}
