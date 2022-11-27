package musichub.demo.DTO;


import lombok.Data;

import java.io.Serializable;

@Data
public class RolesDTO implements Serializable {
    private static final long serialVersionUID = 1L;
    private Integer roleID;

    private String roleName;

}
