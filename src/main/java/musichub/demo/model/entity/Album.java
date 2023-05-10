package musichub.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import musichub.demo.model.EAlbumType;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "album")
public class Album extends BaseEntityUUID {

    @Size(min = 2, max = 30)
    @NotNull
    @Column
    private String name;

    @Column
    private String image;

    @JsonIgnore
    @Column
    private Long view;

    @NotNull
    @Enumerated(EnumType.STRING)
    @Column(name = "type", nullable = false)
    private EAlbumType type;

    @JsonIgnore
    @Column
    private Date dateCreate;

    @Column(name = "isPublic", nullable = false)
    private boolean isPublic;

    //    @OneToOne(mappedBy = "id", cascade = CascadeType.ALL)
    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
    @OneToOne(cascade = CascadeType.DETACH)
    @JoinColumn(name = "albumtypeid", referencedColumnName = "id", unique = true)
    @NotNull
    private AlbumType albumType;

    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "album_song",
            joinColumns =
                    {@JoinColumn(name = "albumid", referencedColumnName = "id")},
            inverseJoinColumns =
                    {@JoinColumn(name = "songid", referencedColumnName = "id")})
    private Set<Song> song = new HashSet<>();


}
