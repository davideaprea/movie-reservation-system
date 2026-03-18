package dto;

import org.springframework.http.HttpStatus;

public record HttpResponse<T>(
        T body,
        HttpStatus status
) {
}
