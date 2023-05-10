package musichub.demo.repository;

import musichub.demo.model.entity.Account;
import musichub.demo.model.entity.Artist;
import musichub.demo.model.entity.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    List<Song> findByIdIn(List<Long> list);


    List<Song> findSongsByAudio(String audio);
}
