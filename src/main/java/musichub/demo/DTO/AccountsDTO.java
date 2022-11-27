package musichub.demo.DTO;


import lombok.Data;

import java.io.Serializable;
import java.sql.Date;

@Data
public class AccountsDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long accountID;

    private String name;

    private Integer gender;

    private Date birthday;

    private String phone;

    private String email;

    private String avatar;

    private String password;

    private String username;

    private Date dateRegister;

    private Integer isArtist;

    private Boolean active;

}
