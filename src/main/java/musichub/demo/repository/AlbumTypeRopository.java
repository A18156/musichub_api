package musichub.demo.repository;


import musichub.demo.model.entity.AlbumType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface AlbumTypeRopository extends JpaRepository<AlbumType, Long> {
    boolean existsByName(String name);
}
