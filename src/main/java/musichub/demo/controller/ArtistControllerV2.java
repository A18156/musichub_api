package musichub.demo.controller;

import musichub.demo.model.entity.Artist;
import musichub.demo.model.dto.Result;
import musichub.demo.repository.AccountRepository;
import musichub.demo.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.UUID;

@RestController
@RequestMapping("/api/artist")
public class ArtistControllerV2 extends CRUDController<ArtistRepository, Artist, UUID> {

    @Autowired
    private AccountRepository accountRepository;

    @Override
    protected Artist merge(Artist oldEntity, Artist updateEntity) {
        oldEntity.setArtistName(updateEntity.getArtistName());
        oldEntity.setImage(updateEntity.getImage());
        oldEntity.setArtistType(updateEntity.getArtistType());
        oldEntity.setDescription(updateEntity.getDescription());
        oldEntity.setAccount(updateEntity.getAccount());
        return oldEntity;
    }

    @Override
    protected boolean checkExist(Artist newEntity) {
        return newEntity.getArtistID() != null
                && this.repository.existsById(newEntity.getArtistID());
    }



    @Override
    public ResponseEntity<Result<Artist>> create(@Valid @RequestBody Artist newEntity) {
        if (newEntity.getAccount() == null
                || !accountRepository.existsById(newEntity.getAccount().getAccountID())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'account' invalid"));
        }
        if (repository.existsArtistByAccount(newEntity.getAccount())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'account' not found"));
        }
        return super.create(newEntity);
    }

    @Override
    public ResponseEntity<Result<Void>> update(@PathVariable UUID id, @Valid @RequestBody Artist updateEntity) {
        if (updateEntity.getAccount() == null
                || !accountRepository.existsById(updateEntity.getAccount().getAccountID())) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'account' invalid"));
        }
        if (!repository.existsArtistByArtistIDAndAccount(id, updateEntity.getAccount())
                && repository.existsArtistByAccount(updateEntity.getAccount())
        ) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(Result.badRequest("'account' was map with any artist before"));
        }
        return super.update(id, updateEntity);
    }
}
