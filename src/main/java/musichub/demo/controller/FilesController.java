package musichub.demo.controller;

import java.util.List;
import java.util.stream.Collectors;

import lombok.extern.slf4j.Slf4j;
import lombok.var;
import musichub.demo.model.FileInfo;
import musichub.demo.model.dto.Result;
import musichub.demo.service.FilesStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.MvcUriComponentsBuilder;

@Slf4j
@RestController
@CrossOrigin(origins = "http://localhost:8090", maxAge = 3600, allowCredentials = "true")
@RequestMapping("/api/file")
public class FilesController {

    @Autowired
    FilesStorageService storageService;

    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER"})
    @PostMapping("/upload")
    public ResponseEntity<Result<String>> uploadFile(@RequestParam("file") MultipartFile file) {
        String message = "";
        try {
            var filename = storageService.save(file);
            log.info("Uploaded the file successfully: " + file.getOriginalFilename());
            return ResponseEntity.status(HttpStatus.OK).body(Result.success(filename));
        } catch (Exception e) {
            log.info("Could not upload the file: " + file.getOriginalFilename() + ". Error: " + e.getMessage());
            return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(Result.error(e.getMessage()));
        }
    }

    @Secured({"ROLE_ADMIN", "ROLE_USER", "ROLE_MANAGER"})
    @GetMapping("/files")
    public ResponseEntity<List<FileInfo>> getListFiles() {
        List<FileInfo> fileInfos = storageService.loadAll().map(path -> {
            String filename = path.getFileName().toString();
            String url = MvcUriComponentsBuilder
                    .fromMethodName(FilesController.class, "getFile", path.getFileName().toString()).build().toString();

            return new FileInfo(filename, url);
        }).collect(Collectors.toList());

        return ResponseEntity.status(HttpStatus.OK).body(fileInfos);
    }
}
