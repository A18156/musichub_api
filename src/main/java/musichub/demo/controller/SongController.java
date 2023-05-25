package musichub.demo.controller;

import lombok.var;
import musichub.demo.model.dto.Result;
import musichub.demo.model.entity.Song;
import musichub.demo.model.entity.SongType;
import musichub.demo.repository.AccountRepository;
import musichub.demo.repository.SongRepository;
import musichub.demo.repository.Song_TypeRepository;
import musichub.demo.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.sql.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/song")
public class SongController extends CRUDController<SongRepository, Song, Long> {
    @Autowired
    private AccountRepository accountRepository;

    @Autowired
    private Song_TypeRepository song_typeRepository;

    @Override
    protected Song merge(Song oldEntity, Song updateEntity) {
        //get current user
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userDetails = (UserDetailsImpl) principal;

        oldEntity.setAudio(updateEntity.getAudio());
        oldEntity.setTitle(updateEntity.getTitle());
        oldEntity.setImage(updateEntity.getImage());
        oldEntity.setAccountid(updateEntity.getAccountid());
        oldEntity.setDateUpload(updateEntity.getDateUpload());
        oldEntity.setPrice(updateEntity.getPrice());
        oldEntity.setIsPublic(updateEntity.getIsPublic());
        oldEntity.setAccountid(updateEntity.getAccountid());
        oldEntity.setSongType(updateEntity.getSongType());
        return oldEntity;
    }

    @Override
    protected boolean checkExist(Song newEntity) {
        return newEntity.getId() != null
                && this.repository.existsById(newEntity.getId());
    }

    @Override
    public ResponseEntity<Result<Song>> create(@Valid @RequestBody Song newEntity) {

        if (!song_typeRepository.existsById(newEntity.getSongType().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'type' not exist"));
        }
        if (newEntity.getAccountid() == null
                || !accountRepository.existsById(newEntity.getAccountid().getAccountID())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'account' invalid"));
        }
        return super.create(newEntity);
    }

    @Override
    public ResponseEntity<Result<Void>> update(@PathVariable Long id, @Valid @RequestBody Song updateEntity) {
        if (!song_typeRepository.existsById(updateEntity.getSongType().getId())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'type' not exist"));
        }
        return super.update(id, updateEntity);
    }
}
