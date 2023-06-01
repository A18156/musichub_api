package musichub.demo.controller;

import io.jsonwebtoken.lang.Assert;
import lombok.var;
import musichub.demo.model.dto.*;
import musichub.demo.service.UserDetailsImpl;
import musichub.demo.model.entity.Account;
import musichub.demo.model.ERole;
import musichub.demo.model.entity.Role;
import musichub.demo.payload.response.MessageResponse;
import musichub.demo.payload.response.UserInfoResponse;
import musichub.demo.repository.AccountRepository;
import musichub.demo.repository.RoleRepository;
import musichub.demo.service.*;
import musichub.demo.util.Responses;
import org.apache.commons.lang.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.*;
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
    AccountRepository accountRepository;

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
        if (accountRepository.existsByPhone(signUpRequest.getPhone())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already taken!"));
        }
        if (accountRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Username is already taken!"));
        }

        if (accountRepository.existsByEmail(signUpRequest.getEmail())) {
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

        Set<Role> roles = new HashSet<>();

        Role userRole = roleRepository.findByName(ERole.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
        roles.add(userRole);

        user.setRoles(roles);
        user.setActive(true);
        user.setAvatar("avatar.png");
        user.setDateRegister(Date.valueOf(java.time.LocalDate.now()));
        accountRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("User registered successfully!"));
    }


    @Secured({"ROLE_USER"})
    @PostMapping("/updateInfo")
    public ResponseEntity<?> updateInfo(@Valid @RequestBody UpdateClientInfoRequest updateClientInfoRequest) {
        UserDetailsImpl authentication = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Long accountID = authentication.getAccountID();

        var currentAccountDetail = accountRepository.findAccountByAccountID(accountID);
        var oldEmail = currentAccountDetail.getEmail();
        var newEmail = updateClientInfoRequest.getEmail();
        if (!Objects.equals(newEmail, oldEmail) && accountRepository.existsByEmailAndAccountIDNot(newEmail, accountID)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Email is already in use!"));
        }
        var oldPhone = currentAccountDetail.getPhone();
        var newPhone = updateClientInfoRequest.getPhone();
        if (!Objects.equals(newPhone, oldPhone) && accountRepository.existsByPhoneAndAccountIDNot(newPhone, accountID)) {
            return ResponseEntity.badRequest().body(new MessageResponse("Error: Phone number is already taken!"));
        }
        currentAccountDetail.setName(updateClientInfoRequest.getName());
        currentAccountDetail.setBirthday(updateClientInfoRequest.getBirthday());
        currentAccountDetail.setEmail(updateClientInfoRequest.getEmail());
        currentAccountDetail.setPhone(updateClientInfoRequest.getPhone());
        currentAccountDetail.setGender(updateClientInfoRequest.getGender());

        accountRepository.save(currentAccountDetail);

        return ResponseEntity.ok(new MessageResponse("update info successfully!"));
    }


    @PostMapping("/forgot-password")
    public ResponseEntity<?> forgotPwd(@Valid @RequestBody MailRequest request) {
        Matcher matcher = EMAIL_PATTERN.matcher(request.getEmail());
        if (matcher.matches()) {
            var account = accountRepository.findAccountByEmail(request.getEmail());
            if (account == null) {
                return ResponseEntity.badRequest().body(new MessageResponse("user not found!"));
            }
            String randomPwd = RandomStringUtils.randomAlphanumeric(6);
            account.setPassword(encoder.encode(randomPwd));
            accountRepository.save(account);
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

        var currentAccountDetail = accountRepository.findAccountByAccountID(getID);

        if (!encoder.matches(oldPwd, currentAccountDetail.getPassword())) {
            return ResponseEntity.badRequest().body(new MessageResponse("you entered the wrong old password!!"));
        }
        currentAccountDetail.setPassword(encoder.encode(newPwd));
        accountRepository.save(currentAccountDetail);
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

    @Secured({"ROLE_ADMIN", "ROLE_MANAGER"})
    @GetMapping("/all-user")
    public ResponseEntity<Result<List<Account>>> getAll() {
        return ResponseEntity.ok(Result.success(accountRepository.findAll()));
    }

    @PostMapping("/create-account")
    public ResponseEntity<?> createUser(@Valid @RequestBody AccountDTO request) {
        if (!StringUtils.hasText(request.getPassword())) {
            return Responses.badRequest("Error: Invalid password!");
        }
        if (accountRepository.existsByPhone(request.getPhone())) {
            return Responses.badRequest("Error: Phone number is already taken!");
        }
        if (accountRepository.existsByUsername(request.getUsername())) {
            return Responses.badRequest("Error: Username is already taken!");
        }
        if (accountRepository.existsByEmail(request.getEmail())) {
            return Responses.badRequest("Error: Email is already in use!");
        }

        // Create new user's account
        Account newAccount = request.toRawAccount();
        newAccount.encodePwd(encoder);
        ERole strRole = ERole.of(request.getRole());
        return roleRepository.findByName(strRole)
                .map(role -> {
                    newAccount.setAccountID(null);
                    newAccount.setRoles(Collections.singleton(role));
                    return accountRepository.saveAndFlush(newAccount);
                })
                .map(saved -> Responses.ok("saved"))
                .orElseGet(() -> Responses.badRequest("Error: Role is not found."));

    }


    @DeleteMapping("delete-account/{id}")
    public ResponseEntity<?> deleteAccount(@PathVariable Long id) {
        accountRepository.deleteById(id);
        return ResponseEntity.ok().body(Result.success());
    }

    @PutMapping("update-account/{id}")
    public ResponseEntity<?> updateAccount(@PathVariable Long id, @Valid @RequestBody AccountDTO request) {
        Account account = request.toRawAccount();
        account.setAccountID(id);
        if (accountRepository.existsByEmailAndAccountIDNot(account.getPhone(), id)) {
            return Responses.badRequest("Error: Phone number is already taken!");
        }
        if (accountRepository.existsByEmailAndAccountIDNot(account.getEmail(), id)) {
            return Responses.badRequest("Error: Email is already in use!");
        }

        Optional<Account> accountOpt = accountRepository.findById(id);
        return accountOpt.map(oldAccount -> roleRepository
                        .findByName(ERole.of(request.getRole()))
                        .map(role -> {
                            oldAccount.setAvatar(account.getAvatar());
                            oldAccount.setBirthday(account.getBirthday());
                            oldAccount.setEmail(account.getEmail());
                            oldAccount.setPhone(account.getPhone());
                            oldAccount.setGender(account.getGender());
                            oldAccount.setRoles(new HashSet<>(Collections.singleton(role)));
                            oldAccount.setActive(account.isActive());
                            oldAccount.setArtist(account.isArtist());
                            return accountRepository.saveAndFlush(oldAccount);
                        })
                        .map(saved -> Responses.ok("updated"))
                        .orElseGet(() -> Responses.badRequest("Error: Role is not found."))
                )
                .orElseGet(() -> Responses.badRequest("account not found"));
    }

    @GetMapping("get-by-id/{id}")
    public ResponseEntity<?> getOne(@PathVariable Long id) {
        Optional<Account> account = accountRepository.findById(id);
        if (!account.isPresent()) {
            return Responses.badRequest("account not found");
        }
        AccountDTO dto = new AccountDTO(account.get());
        return ResponseEntity.ok(dto);
    }

}
