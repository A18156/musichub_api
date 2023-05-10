package musichub.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor(staticName = "of")
public class Result<T> {
    private int status;
    private String message;
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private T data;

    public static <R> Result<R> success() {
        return success(null);
    }

    public static <R> Result<R> success(R data) {
        return of(200, "Success", data);
    }

    public static <R> Result<R> notFound() {
        return of(404, "Not found", null);
    }

    public static <R> Result<R> badRequest(String error) {
        return of(400, error, null);
    }

    public static <R> Result<R> conflict() {
        return of(409, "Conflict", null);
    }

    public static <R> Result<R> unauthorized(String message) {
        return of(401, message, null);
    }

    public static <R> Result<R> forbidden(String message) {
        return of(403, message, null);
    }

    public static <R> Result<R> error(String message) {
        return of(500, message, null);
    }
}
