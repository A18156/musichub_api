package musichub.demo.controller;

import io.jsonwebtoken.lang.Assert;
import lombok.var;
import musichub.demo.model.dto.MailRequest;
import musichub.demo.model.dto.UpdateClientInfoRequest;
import musichub.demo.service.UserDetailsImpl;
import musichub.demo.model.entity.Account;
import musichub.demo.model.ERole;
import musichub.demo.model.entity.Role;
import musichub.demo.model.dto.LoginRequest;
import musichub.demo.model.dto.SignupRequest;
import musichub.demo.payload.response.MessageResponse;
import musichub.demo.payload.response.UserInfoResponse;
import musichub.demo.repository.AccountRepository;
import musichub.demo.repository.RoleRepository;
import musichub.demo.service.*;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000"}, maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    public static final Pattern EMAIL_PATTERN = Pattern.compile("^[a-zA-Z0-9_!#$%&'*+/=?`{|}~^.-]+@[a-zA-Z0-9.-]+$");
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    AccountRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    private EmailSenderService senderService;

    @Autowired
    JwtUtils jwtUtils;

    //    @GetMapping("/getuser")
//    public ResponseEntity<?> username(Authentication authentication){
//        var userDetails = (UserDetailsImpl) authentication.getPrincipal();
//        return ResponseEntity.ok().body(userDetails.getAccountID());
//    }
    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        var authentication = authenticationManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        var userDetails = (UserDetailsImpl) authentication.getPrincipal();

        var jwtCookie = jwtUtils.generateJwtCookie(userDetails);

        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, jwtCookie.toString())
                .body(new UserInfoResponse(userDetails.getAccountID(),
                        userDetails.getName(),
                        userDetails.getGender(),
                        userDetails.getBirthday(),
                        userDetails.getPhone(),
                        userDetails.getEmail(),
                        userDetails.getAvatar(),
                        userDetails.getUsername(),
                        userDetails.getDateRegister(),
                        userDetails.isArtist(),
                        userDetails.isActive(),
                        userDetails.getPackageTerm(),
                        roles));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByPhone(signUpRequest.getPhone())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already taken!"));
        }
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (userRepository.existsByEmail(signUpRequest.getEmail())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }

        // Create new user's account
        var user = new Account(
                signUpRequest.getName(),
                signUpRequest.getGender(),
                signUpRequest.getBirthday(),
                signUpRequest.getPhone(),
                signUpRequest.getEmail(),
                encoder.encode(signUpRequest.getPassword()),
                signUpRequest.getUsername());

        Set<String> strRoles = signUpRequest.getRole();
        Set<Role> roles = new HashSet<>();

        if (strRoles == null) {
            Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
            roles.add(userRole);
        } else {
            strRoles.forEach(role -> {
                switch (role) {
                    case "admin":
                        Role adminRole = roleRepository.findByName(ERole.ROLE_ADMIN)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(adminRole);

                        break;
                    case "mod":
                        Role modRole = roleRepository.findByName(ERole.ROLE_MANAGER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(modRole);

                        break;
                    default:
                        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
                        roles.add(userRole);
                }
            });
        }

        user.setRoles(roles);
        user.setActive(true);
        user.setAvatar("avatar.png");
        user.setDateRegister(Date.valueOf(java.time.LocalDate.now()));
        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @Secured({"ROLE_USER"})
    @PostMapping("/updateInfo")
    public ResponseEntity<?> updateInfo(@Valid @RequestBody UpdateClientInfoRequest updateClientInfoRequest) {
        UserDetailsImpl authentication = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long accountID = authentication.getAccountID();

        var currentAccountDetail = userRepository.findAccountByAccountID(accountID);
        var oldEmail = currentAccountDetail.getEmail();
        var newEmail = updateClientInfoRequest.getEmail();
        if (!Objects.equals(newEmail, oldEmail) && userRepository.countByEmailAndAccountIDNot(newEmail, accountID) > 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        var oldPhone = currentAccountDetail.getPhone();
        var newPhone = updateClientInfoRequest.getPhone();
        if (!Objects.equals(newPhone, oldPhone) && userRepository.countByPhoneAndAccountIDNot(newPhone, accountID) > 0) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already taken!"));
        }
        currentAccountDetail.setName(updateClientInfoRequest.getName());
        currentAccountDetail.setBirthday(updateClientInfoRequest.getBirthday());
        currentAccountDetail.setEmail(updateClientInfoRequest.getEmail());
        currentAccountDetail.setPhone(updateClientInfoRequest.getPhone());
        currentAccountDetail.setGender(updateClientInfoRequest.getGender());

        userRepository.save(currentAccountDetail);

        return ResponseEntity.ok(new MessageResponse("update info successfully!"));
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPwd(@Valid @RequestBody MailRequest request) {
        Matcher matcher = EMAIL_PATTERN.matcher(request.getEmail());
        if (matcher.matches()) {
            var account = userRepository.findAccountByEmail(request.getEmail());
            if (account == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("user not found!"));
            }
            String randomPwd = RandomStringUtils.randomAlphanumeric(6);
            account.setPassword(encoder.encode(randomPwd));
            userRepository.save(account);
            String subject = "MusicHub - Reset Password";
            String body = "Hello,\n\nNew password is " + randomPwd;
            senderService.sendEmail(request.getEmail(), subject, body);
            return ResponseEntity.ok(new MessageResponse("mail sent successful!!"));
        }
        return ResponseEntity.badRequest().body(new MessageResponse("email invalid"));
    }

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER", "ROLE_USER"})
    @PostMapping("/change-password")
    public ResponseEntity<?> changePwd(@Valid @RequestBody ChangePasswordRequest changePasswordRequest) {
        String newPwd = changePasswordRequest.getNewPassword();
        String oldPwd = changePasswordRequest.getOldPassword();
        Assert.hasLength(newPwd);
        Assert.hasLength(oldPwd);

        if (newPwd.equals(oldPwd)) {
            return ResponseEntity.badRequest().body(new MessageResponse("new password not equal old password!!"));
        }

        UserDetailsImpl authentication = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long getID = authentication.getAccountID();

        var currentAccountDetail = userRepository.findAccountByAccountID(getID);

        if (!encoder.matches(oldPwd, currentAccountDetail.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("you entered the wrong old password!!"));
        }
        currentAccountDetail.setPassword(encoder.encode(newPwd));
        userRepository.save(currentAccountDetail);
        return ResponseEntity.ok(new MessageResponse("change password successful!!"));
    }


    @PostMapping("/signout")
    public ResponseEntity<?> logoutUser() {
        ResponseCookie cookie = jwtUtils.getCleanJwtCookie();
        return ResponseEntity.ok().header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body(new MessageResponse("You've been signed out!"));
    }

    @GetMapping("/me")
    public ResponseEntity<UserInfoResponse> getMe() {
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (!(principal instanceof UserDetailsImpl)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }
        var userDetails = (UserDetailsImpl) principal;
        var roles = userDetails.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.toList());
        return ResponseEntity.ok()
                .body(new UserInfoResponse(
                        userDetails.getAccountID(),
                        userDetails.getName(),
                        userDetails.getGender(),
                        userDetails.getBirthday(),
                        userDetails.getPhone(),
                        userDetails.getEmail(),
                        userDetails.getAvatar(),
                        userDetails.getUsername(),
                        userDetails.getDateRegister(),
                        userDetails.isArtist(),
                        userDetails.isActive(),
                        userDetails.getPackageTerm(),
                        roles
                ));
    }

}
