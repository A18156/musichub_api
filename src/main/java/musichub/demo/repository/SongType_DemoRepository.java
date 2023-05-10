package musichub.demo.repository;

import musichub.demo.model.entity.SongType_Demo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

@Repository
public interface SongType_DemoRepository extends JpaRepository<SongType_Demo, UUID> {
}
