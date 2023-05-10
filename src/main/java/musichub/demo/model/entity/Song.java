package musichub.demo.model.entity;


import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;
import java.util.HashSet;
import java.util.Set;

@Data
@Entity
@AllArgsConstructor
@NoArgsConstructor
@JsonIgnoreProperties(value = {"handler", "hibernateLazyInitializer", "FieldHandler"})
@Table(name = "song")
public class Song extends BaseEntity<Long> {

    @NotNull
    @Size(min = 2, max = 30)
    @Column
    private String name;

    @JsonIgnore
    @Column
    private Date dateUpload;

    @Column(nullable = true)
    private String image;

    @NotNull
    @Column
    private String audio;

//    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
//    @JoinColumn(name = "accountID")
////    @OneToMany(cascade = CascadeType.DETACH)
//    @OneToMany(fetch = FetchType.LAZY)
//    @OnDelete(action = OnDeleteAction.CASCADE)
//    private Set<Account> account = new HashSet<>();

    @JsonIgnore
    @Column(nullable = false)
    private Long accountid;

    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "song_artist",
            inverseJoinColumns =
                    {@JoinColumn(name = "songid")},
            joinColumns =
                    {@JoinColumn(name = "artistid")})
    private Set<Artist> artist = new HashSet<>();

    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "song_songType",
            inverseJoinColumns =
                    {@JoinColumn(name = "songid")},
            joinColumns =
                    {@JoinColumn(name = "songtypeid")})
    private Set<SongType> songtype = new HashSet<>();
}
