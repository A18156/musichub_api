package musichub.demo.payload.response;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserInfoResponse {
    private Long accountID;

    private String name;

    private Integer gender;
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private Date birthday;

    private String phone;

    private String email;

    private String avatar;

    private String username;

    private Date dateRegister;

    @JsonProperty("isArtist")
    private boolean isArtist;

    private boolean active;

    private Date packageTerm;

    private List<String> roles;

}
