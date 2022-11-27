package musichub.demo.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@Table(name = "account",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = "username"),
                @UniqueConstraint(columnNames = "email")
        })
public class Account implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "accountID", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long accountID;

    @NotBlank
    @Size(min = 3, max = 30)
    @Column(nullable = false)
    private String name;

    @NotNull
    @Column(nullable = false)
    private Integer gender;

//    @NotBlank
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
//    @DateTimeFormat(pattern = "dd-mm-yyyy")
    @Column
    private Date birthday;

    @NotBlank
    @Size(min = 10, max = 10)
    @Column(nullable = false)
    private String phone;

    @NotBlank
    @Email
    @Size(max = 50)
    @Column(name = "email", nullable = false)
    private String email;

    @Column(name = "avatar")
    private String avatar;

    @NotNull
//    @Size(max = 120)
    @Column(name = "password")
    private String password;

    @NotBlank
    @Column(name = "username", nullable = false)
    private String username;

    @Column(name = "dateRegister")
    private Date dateRegister;

    @Column(name = "isArtist")
    private Integer isArtist;

    @Column(name = "active")
    private Boolean active;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "account_roles",
            joinColumns = @JoinColumn(name = "accountID"),
            inverseJoinColumns = @JoinColumn(name = "roleID"))
    private Set<Roles> roles = new HashSet<>();

    public Account() {
    }

    public Account(String name, Integer gender, Date birthday, String phone, String email, String password, String username) {
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.password = password;
        this.username = username;
    }

    public Set<Roles> getRoles() {
        return roles;
    }

    public void setRoles(Set<Roles> roles) {
        this.roles = roles;
    }
}
