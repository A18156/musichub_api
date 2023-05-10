package musichub.demo.controller;

import musichub.demo.model.dto.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

@CrossOrigin(
        origins = {"http://localhost:8080", "http://localhost:3000"},
        maxAge = 3600,
        allowCredentials = "true"
)
public abstract class CRUDController<R extends JpaRepository<E, I>, E, I> {

    @Autowired
    protected R repository;

    @GetMapping
    public ResponseEntity<Result<List<E>>> getAll() {
        return ResponseEntity.ok(Result.success(repository.findAll()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<Result<E>> getOne(@PathVariable I id) {
        return repository.findById(id)
                .map(Result::success)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity
                        .status(HttpStatus.NOT_FOUND)
                        .body(Result.notFound())
                );
    }

    @Secured("ROLE_ADMIN")
    @PostMapping
    public ResponseEntity<Result<E>> create(@Valid @RequestBody E newEntity) {
        if (checkExist(newEntity)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Result.conflict());
        }
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(Result.success(repository.save(newEntity)));
    }

    @Secured("ROLE_ADMIN")
    @PutMapping("/{id}")
    public ResponseEntity<Result<Void>> update(@PathVariable I id, @Valid @RequestBody E updateEntity) {
        var optional = repository.findById(id);
        if (optional.isEmpty()) {
            return ResponseEntity
                    .status(HttpStatus.NOT_FOUND)
                    .body(Result.notFound());
        }
        var mergeEntity = merge(optional.get(), updateEntity);
        repository.saveAndFlush(mergeEntity);
        return ResponseEntity.ok().body(
                Result.success()
        );
    }

    @Secured({"ROLE_ADMIN"})
    @DeleteMapping("/{id}")
    public ResponseEntity<Result<Void>> delete(@PathVariable I id) {
        repository.deleteById(id);
        return ResponseEntity.ok().body(Result.success());
    }

    protected abstract E merge(E oldEntity, E updateEntity);

    protected abstract boolean checkExist(E newEntity);
}
