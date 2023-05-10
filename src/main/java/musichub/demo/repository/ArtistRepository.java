package musichub.demo.repository;

import musichub.demo.model.entity.Account;
import musichub.demo.model.entity.Artist;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ArtistRepository extends JpaRepository<Artist, UUID> {
    Boolean existsArtistByAccount(Account account);
    Boolean existsArtistByArtistIDAndAccount(UUID id, Account account);

    //v1
    Boolean existsArtistByArtistID(UUID id);

    Artist findArtistByArtistID(UUID id);

    List<Artist> findByArtistIDIn(List<UUID> list);
}
