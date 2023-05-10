package musichub.demo.repository;

import musichub.demo.model.entity.SongType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface Song_TypeRepository extends JpaRepository<SongType, Long> {
    Boolean existsByName(String name);

    List<SongType> findByIdIn(List<Long> collect);
}