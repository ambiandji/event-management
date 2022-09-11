package org.crok4it.em.exception;

public enum ErrorCode {
    ARTIST_NOT_FOUND(1000),
    ARTIST_CONFLICT(1001),
    ARTIST_INVALID(1002)

    ;

    private final int code;

    ErrorCode(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
