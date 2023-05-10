package musichub.demo.controller;

import musichub.demo.model.dto.Result;
import musichub.demo.model.entity.Album;
import musichub.demo.model.entity.Artist;
import musichub.demo.repository.AccountRepository;
import musichub.demo.repository.AlbumRepository;
import musichub.demo.repository.ArtistRepository;
import musichub.demo.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000"}, maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/album")
public class AlbumController extends CRUDController<AlbumRepository, Album, UUID> {
    @Autowired
    private SongRepository songRepository;
    @Override
    protected Album merge(Album oldEntity, Album updateEntity) {
        oldEntity.setName(updateEntity.getName());
        oldEntity.setImage(updateEntity.getImage());
        oldEntity.setType(updateEntity.getType());
        oldEntity.setPublic(updateEntity.isPublic());
        oldEntity.setAlbumType(updateEntity.getAlbumType());
        oldEntity.setSong(updateEntity.getSong());
        oldEntity.setDateCreate(Date.valueOf(java.time.LocalDate.now()));
        oldEntity.setView(0L);
        return oldEntity;
    }

    @Override
    protected boolean checkExist(Album newEntity) {
        return newEntity.getId() != null && this.repository.existsById(newEntity.getId());
    }

    @Override
    public ResponseEntity<Result<Album>> create(@Valid @RequestBody Album newEntity){
        if(songRepository.findByIdIn(newEntity.getSong()
                .stream().map(
                id -> id.getId()).filter(
                        uuid -> uuid != null)
                .collect(Collectors.toList()
        )).isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.badRequest("song not exits"));
        }
        return super.create(newEntity);
    }
//
    @Override
    public ResponseEntity<Result<Void>> update(@PathVariable UUID id, @Valid @RequestBody Album updateEntity){
        if(songRepository.findByIdIn(updateEntity.getSong()
                .stream().map(
                        songid -> songid.getId()).filter(
                        uuid -> uuid != null)
                .collect(Collectors.toList()
                )).isEmpty()){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.badRequest("song not exits"));
        }
        return super.update(id,updateEntity);
    }
}
