package musichub.demo.controller;

import musichub.demo.model.dto.Result;
import musichub.demo.model.entity.AlbumType;
import musichub.demo.repository.AlbumTypeRopository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000"}, maxAge = 3600, allowCredentials = "true")
@RestController
@RequestMapping("/api/albumtype")
public class AlbumTypeController extends CRUDController<AlbumTypeRopository, AlbumType,Long>{

    @Override
    protected AlbumType merge(AlbumType oldEntity, AlbumType updateEntity) {
        oldEntity.setName(updateEntity.getName());
        return oldEntity;
    }

    @Override
    protected boolean checkExist(AlbumType newEntity) {
        return newEntity.getId() != null
                && this.repository.existsById(newEntity.getId());
    }

    @Override
    public ResponseEntity<Result<AlbumType>> create(@Valid @RequestBody AlbumType newEntity){
        if(this.repository.existsByName(newEntity.getName())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.badRequest("name already exist"));
        }
        return super.create(newEntity);
    }

    @Override
    public ResponseEntity<Result<Void>> update(@PathVariable Long id, @Valid @RequestBody AlbumType upddateEntity){
        if(this.repository.existsByName(upddateEntity.getName())){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Result.badRequest("name already exist"));
        }
        return super.update(id, upddateEntity);
    }
}
