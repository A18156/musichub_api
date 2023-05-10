package musichub.demo.controller;

import musichub.demo.model.dto.Result;
import musichub.demo.model.entity.Song;
import musichub.demo.repository.AccountRepository;
import musichub.demo.repository.ArtistRepository;
import musichub.demo.repository.SongRepository;
import musichub.demo.repository.Song_TypeRepository;
import musichub.demo.service.UserDetailsImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.sql.Date;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/song")
public class SongController extends CRUDController<SongRepository, Song, Long>{
    @Autowired
    private AccountRepository accountRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private Song_TypeRepository song_typeRepository;
    @Override
    protected Song merge(Song oldEntity, Song updateEntity) {
        //get current user
        var principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        var userDetails = (UserDetailsImpl) principal;

        oldEntity.setAudio(updateEntity.getAudio());
        oldEntity.setName(updateEntity.getName());
        oldEntity.setImage(updateEntity.getImage());
//        oldEntity.setAccount(updateEntity.getAccount());
        oldEntity.setAccountid(userDetails.getAccountID());
        oldEntity.setArtist(updateEntity.getArtist());
        oldEntity.setSongtype(updateEntity.getSongtype());
        oldEntity.setDateUpload(Date.valueOf(java.time.LocalDate.now()));
        return oldEntity;
    }

    @Override
    protected boolean checkExist(Song newEntity) {
        return newEntity.getId() != null
                && this.repository.existsById(newEntity.getId());
    }

    @Override
    public ResponseEntity<Result<Song>> create(@Valid @RequestBody Song newEntity){
        if (accountRepository.findById(newEntity.getAccountid()).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'account' not exist"));
        }
        if (newEntity.getArtist() == null || artistRepository.findByArtistIDIn(newEntity.getArtist()
                .stream().map(artistID -> artistID.getArtistID())
                .filter(uuid -> uuid != null)
                .collect(Collectors.toList())).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'artist' not exist"));
        }
        if (song_typeRepository.findByIdIn(newEntity.getSongtype()
                .stream().map(stid -> stid.getId())
                .filter(stid -> stid !=null)
                .collect(Collectors.toList())).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'type' not exist"));
        }

        return super.create(newEntity);
    }

    @Override
    public ResponseEntity<Result<Void>> update(@PathVariable Long id, @Valid @RequestBody Song updateEntity){
        if ( accountRepository.findById(updateEntity.getAccountid()).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'account' not exist"));
        }
        if (updateEntity.getArtist() == null || artistRepository.findByArtistIDIn(updateEntity.getArtist()
                .stream().map(artistID -> artistID.getArtistID())
                .filter(uuid -> uuid != null)
                .collect(Collectors.toList())).isEmpty()
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'artist' not exist"));
        }
        if (song_typeRepository.findByIdIn(updateEntity.getSongtype()
                .stream().map(stid -> stid.getId())
                .filter(stid -> stid !=null)
                .collect(Collectors.toList())).isEmpty()) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'type' not exist"));
        }
        return super.update(id, updateEntity);
    }
}
