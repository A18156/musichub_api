//package musichub.demo.controller;
//
//
//import musichub.demo.model.Artist;
//import musichub.demo.payload.request.F_ArtistRequest;
//import musichub.demo.payload.response.MessageResponse;
//import musichub.demo.repository.ArtistRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import javax.validation.Valid;
//
//import static org.springframework.util.StringUtils.capitalize;
//
//@CrossOrigin(origins = {"http://localhost:8080", "http://localhost:3000"}, maxAge = 3600, allowCredentials = "true")
//@RestController
//@RequestMapping("/api/auth")
//public class ArtistController {
//    @Autowired
//    ArtistRepository artistRepository;
//
//    @PostMapping("/artist")
//    public ResponseEntity<?> create(@Valid @RequestBody F_ArtistRequest f_artistRequest){
//        if(artistRepository.existsArtistByAccountId(f_artistRequest.getAccountId())){
//            return ResponseEntity.badRequest().body(new MessageResponse("Error: Account is already in use!"));
//        }else {
//                var artistID = "ART" + (artistRepository.count() + 1);
//            var artist = new Artist(
//                    artistID.toString().trim(),
//                    capitalize(f_artistRequest.getArtistName()),
//                    f_artistRequest.getImage(),
//                    f_artistRequest.getArtistType(),
//                    f_artistRequest.getAccountId(),
//                    f_artistRequest.getDescription()
//            );
//
////        artist.setArtistID(artistID);
//            artistRepository.save(artist);
//            return ResponseEntity.ok(new MessageResponse("Artist create successfully"));
//        }
//    }
//
//    @PutMapping("/artist/{id}")
//    public ResponseEntity<?> updateDemo(@PathVariable String id,@Valid @RequestBody Artist artistDetail){
//        if (artistRepository.existsArtistByArtistID(id)){
//            var artist = artistRepository.findArtistByArtistID(id);
//            artist.setArtistName(capitalize(artistDetail.getArtistName()));
//            artist.setImage(artistDetail.getImage());
//            artist.setArtistType( artistDetail.getArtistType());
//            artist.setAccountId(artistDetail.getAccountId());
//            artist.setDescription(artistDetail.getDescription());
//            artistRepository.save(artist);
//            return ResponseEntity.ok(new MessageResponse("Artist update successfully"));
//        }else return ResponseEntity.ok(new MessageResponse("Artist is not exist"));
//    }
//
//    @DeleteMapping("/artist/{id}")
//    public ResponseEntity<?> DeleteArtistById(@PathVariable String id){
//        if(artistRepository.existsArtistByArtistID(id)){
//            var artist = artistRepository.findArtistByArtistID(id);
//            artistRepository.delete(artist);
//            return ResponseEntity.ok("Artist delete successfully");
//        }else return ResponseEntity.ok(new MessageResponse("Artist is not exist"));
//    }
//
//    @GetMapping("/artist")
//    public ResponseEntity<?> showALl(){
//        return ResponseEntity.ok(artistRepository.findAll());
//    }
//}
