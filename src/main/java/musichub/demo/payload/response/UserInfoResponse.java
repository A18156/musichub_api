package musichub.demo.payload.request.response;

import java.sql.Date;
import java.util.List;

public class UserInfoResponse {
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
    private List<String> roles;

    public UserInfoResponse(Long accountID, String name, Integer gender, Date birthday, String phone, String email, String avatar, String password, String username, Date dateRegister, Integer isArtist, Boolean active, List<String> roles) {
        this.accountID = accountID;
        this.name = name;
        this.gender = gender;
        this.birthday = birthday;
        this.phone = phone;
        this.email = email;
        this.avatar = avatar;
        this.password = password;
        this.username = username;
        this.dateRegister = dateRegister;
        this.isArtist = isArtist;
        this.active = active;
        this.roles = roles;
    }

    public Long getAccountID() {
        return accountID;
    }

    public void setAccountID(Long accountID) {
        this.accountID = accountID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Integer getGender() {
        return gender;
    }

    public void setGender(Integer gender) {
        this.gender = gender;
    }

    public Date getBirthday() {
        return birthday;
    }

    public void setBirthday(Date birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Date getDateRegister() {
        return dateRegister;
    }

    public void setDateRegister(Date dateRegister) {
        this.dateRegister = dateRegister;
    }

    public Integer getIsArtist() {
        return isArtist;
    }

    public void setIsArtist(Integer isArtist) {
        this.isArtist = isArtist;
    }

    public Boolean getActive() {
        return active;
    }

    public void setActive(Boolean active) {
        this.active = active;
    }

    public List<String> getRoles() {
        return roles;
    }


}
