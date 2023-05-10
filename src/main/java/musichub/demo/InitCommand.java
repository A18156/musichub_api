package musichub.demo;

import lombok.RequiredArgsConstructor;
//import musichub.demo.repository.OrderItemRepository;
import musichub.demo.model.ERole;
import musichub.demo.model.entity.Role;
import musichub.demo.repository.RoleRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class InitCommand implements CommandLineRunner {

    private final RoleRepository repository;
//    private final OrderItemRepository itemRepository;

    @Override
    public void run(String... args) {
        var all = repository.findAll();
        for(var eRole : ERole.values()) {
            createIfNotExist(all, eRole);
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
            repository.save(role);
        }
    }
}
