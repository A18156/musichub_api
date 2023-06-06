package musichub.demo;

import lombok.RequiredArgsConstructor;
//import musichub.demo.repository.OrderItemRepository;
import lombok.var;
import musichub.demo.model.ERole;
import musichub.demo.model.entity.Account;
import musichub.demo.model.entity.Role;
import musichub.demo.model.entity.Song;
import musichub.demo.model.entity.SongType;
import musichub.demo.repository.AccountRepository;
import musichub.demo.repository.RoleRepository;
import musichub.demo.repository.SongRepository;
import musichub.demo.repository.Song_TypeRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class InitCommand implements CommandLineRunner {

    private final RoleRepository roleRepository;
    private final AccountRepository accountRepository;
    private final Song_TypeRepository songTypeRepository;
    private final SongRepository songRepository;
    private final PasswordEncoder encoder;
//    private final OrderItemRepository itemRepository;

    @Override
    public void run(String... args) {
        var all = roleRepository.findAll();
        for (var eRole : ERole.values()) {
            createIfNotExist(all, eRole);
        }
        Account admin = accountRepository.findByUsername("admin")
                .orElseGet(() -> {
                    Account acc = new Account();
                    acc.setUsername("admin");
                    acc.setPassword(encoder.encode("Admin@123"));
                    acc.setName("Admin");
                    acc.setRoles(Collections.singleton(roleRepository.findByName(ERole.ROLE_ADMIN).get()));
                    acc.setActive(true);
                    acc.setPhone("0342300000");
                    acc.setEmail("admin.musichub@gmail.com");
                    acc.setAvatar("avatar.png");
                    acc.setBirthday(Date.valueOf("1998-04-29"));
                    acc.setGender(1); // MALE
                    return accountRepository.save(acc);
                });
        var rockType = songTypeRepository.findByName("ROCK")
                .orElseGet(() -> {
                    SongType type = new SongType();
                    type.setName("ROCK");
                    return songTypeRepository.save(type);
                });
        for (int i = 0; i < 10; i++) {
            String title = "Song-0" + i;
            songRepository.findByTitle(title)
                    .orElseGet(() -> {
                        Song song = new Song();
                        song.setTitle(title);
                        song.setImage("track.jpg");
                        song.setAudio("aa4baa7b-5d0d-4179-b938-902599e3181e.mp3");
                        song.setSongType(rockType);
                        song.setIsPublic(true);
                        song.setPrice(100D);
                        song.setAccountid(admin);
                        song.setDateUpload(new Date(System.currentTimeMillis()));
                        return songRepository.save(song);
                    });
        }
//        itemRepository.saveAll(List.of(
//                new VIPOrderItem().setName("V.I.P PRO").setPrice(10),
//                new SongOrderItem().setName("song b").setPrice(1),
//                new OrderItem().setName("common").setPrice(999)
//        ));
//        var allItem = itemRepository.findAll();
        System.out.println("");
    }

    private void createIfNotExist(List<Role> all, ERole eRole) {
        if (all.stream().noneMatch(it -> it.getName() == eRole)) {
            var role = new Role(eRole);
            roleRepository.save(role);
        }
    }
}
