package musichub.demo.util;

import musichub.demo.payload.response.MessageResponse;
import org.springframework.http.ResponseEntity;

public final class Responses {

    public static ResponseEntity<MessageResponse> ok(String message){
        return ResponseEntity.ok(new MessageResponse(message));
    }

    public static  ResponseEntity<MessageResponse> badRequest(String message){
        return ResponseEntity.badRequest().body(new MessageResponse(message));
    }
}
