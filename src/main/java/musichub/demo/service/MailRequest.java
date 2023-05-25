package musichub.demo.service;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotNull;

@Data
public class MailRequest {
    private String mail;
}
