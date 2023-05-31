package musichub.demo.service;

import musichub.demo.model.entity.Account;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.GrantedAuthority;

import java.sql.Date;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class UserDetailsImpl implements UserDetails {

//    private Accounts accounts;
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

    private boolean isArtist;

    private boolean active;

    private Date packageTerm;
    private Collection<? extends GrantedAuthority> authorities;

    public UserDetailsImpl(Long accountID,
                           String name,
                           int gender,
                           Date birthday,
                           String phone,
                           String avatar,
                           String email,
                           String password,
                           String username,
                           Date dateRegister,
                           boolean isArtist,
                           boolean active,
                           Date packageTerm,
                           Collection<? extends GrantedAuthority> authorities) {
        this.accountID = accountID;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.avatar = avatar;
        this.email = email;
        this.password = password;
        this.username = username;
        this.dateRegister = dateRegister;
        this.isArtist = isArtist;
        this.active = active;
        this.packageTerm = packageTerm;
        this.authorities = authorities;
    }

    public static UserDetailsImpl build(Account user) {
        List<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName().name()))
                .collect(Collectors.toList());

        return new UserDetailsImpl(
                user.getAccountID(),
                user.getName(),
                user.getGender(),
                user.getBirthday(),
                user.getPhone(),
                user.getAvatar(),
                user.getEmail(),
                user.getPassword(),
                user.getUsername(),
                user.getDateRegister(),
                user.isArtist(),
                user.isActive(),
                user.getPackageTerm(),
                authorities);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    public Long getAccountID() {
        return accountID;
    }

    public String getName() {
        return name;
    }

    public Integer getGender() {
        return gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public String getPhone() {
        return phone;
    }

    public String getEmail() {
        return email;
    }

    public String getAvatar() {
        return avatar;
    }

    public Date getPackageTerm(){
        return packageTerm;
    }

    public Date getDateRegister() {
        return dateRegister;
    }

    public boolean isArtist() {
        return isArtist;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return active;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o)
            return true;
        if (o == null || getClass() != o.getClass())
            return false;
        UserDetailsImpl user = (UserDetailsImpl) o;
        return Objects.equals(accountID, user.accountID);
    }
}
