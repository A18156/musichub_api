package musichub.demo.model.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;
import musichub.demo.model.entity.Account;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.ArrayList;

@Data
@NoArgsConstructor
public class AccountDTO {
    private Long id;
    @NotBlank
    @Size(min = 3, max = 30)
    private String name;

    @NotNull
    private Integer gender;

    //    @NotBlank
    @NotNull
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @DateTimeFormat(pattern = "dd-mm-yyyy")
    private Date birthday;

    @NotBlank
    @Size(min = 10, max = 10)
    private String phone;

    @NotBlank
    @Size(max = 50)
    @Email
    private String email;

//    @Size(min = 6, max = 40)
    private String password;

    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    private String role;

    private boolean active;
    @JsonProperty("isArtist")
    private boolean isArtist;

    public AccountDTO(Account account) {
        this.id = account.getAccountID();
        this.name = account.getName();
        this.email = account.getEmail();
        this.phone = account.getPhone();
        this.gender = account.getGender();
        this.birthday = account.getBirthday();
        this.username = account.getUsername();
        this.password = account.getPassword();
        this.active = account.isActive();
        this.isArtist = account.isArtist();
        this.role=new ArrayList<>(account.getRoles()).get(0).getName().getValue();
    }

    public Account toRawAccount() {
        Account account = new Account();
        account.setName(name);
        account.setEmail(email);
        account.setPhone(phone);
        account.setUsername(username);
        account.setPassword(password);
        account.setGender(gender);
        account.setBirthday(birthday);
        account.setAvatar("avatar.png");
        account.setActive(active);
        account.setArtist(isArtist);
        return account;
    }
}