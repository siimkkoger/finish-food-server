package com.ffreaky.apigw.exceptions;

import java.util.Date;

public record ErrorMessageDto(Date timestamp, int status, String errorMessage, String path, Object content) {
}
