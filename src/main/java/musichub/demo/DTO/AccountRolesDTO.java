package musichub.demo.DTO;


import lombok.Data;

import java.io.Serializable;

@Data
public class AccountRolesDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Long accountID;

    private Integer roleID;

}
