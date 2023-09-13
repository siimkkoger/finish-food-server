package com.example.finishfoodserver.exceptions;

import java.util.Date;

public record ErrorMessage(Date timestamp, int status, String errorMessage, String path, Object content) {
}
