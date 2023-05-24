package musichub.demo.controller;

import lombok.var;
import musichub.demo.model.dto.Result;
import musichub.demo.model.entity.SongType;
import musichub.demo.repository.Song_TypeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;


@RestController
@RequestMapping("/api/songtype")
@CrossOrigin(
        origins = {"http://localhost:8080", "http://localhost:3000"},
        maxAge = 3600,
        allowCredentials = "true"
)
public class Song_typeController extends CRUDController<Song_TypeRepository, SongType,Long>{
    @Autowired
    private Song_TypeRepository song_typeRepository;
    @Override
    protected SongType merge(SongType oldEntity, SongType updateEntity) {
        oldEntity.setName(updateEntity.getName().toUpperCase().trim());
        return oldEntity;
    }

    @Override
    protected boolean checkExist(SongType newEntity) {
        return newEntity.getId() != null && this.repository.existsById(newEntity.getId());
    }

    @Override
    public ResponseEntity<Result<SongType>> create(@Valid @RequestBody SongType newEntity){
        if(repository.existsByName(newEntity.getName().toUpperCase().trim())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.badRequest("Name is exist"));
        }
        newEntity.setName(newEntity.getName().toUpperCase());
        return super.create(newEntity);
    }
    @Override
    public ResponseEntity<Result<Void>> update(@PathVariable Long id, @Valid @RequestBody SongType updateEntity){
        if(repository.existsByName(updateEntity.getName().toUpperCase().trim())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.badRequest("Name is exist"));
        }
        updateEntity.setName(updateEntity.getName().toUpperCase().trim());
        return super.update(id, updateEntity);
    }
}
