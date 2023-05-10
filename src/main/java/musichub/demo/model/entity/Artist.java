package musichub.demo.model.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import musichub.demo.model.ArtistType;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.Type;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.UUID;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "artist")
public class Artist {


    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(
            name = "UUID",
            strategy = "org.hibernate.id.UUIDGenerator"
    )
    @Type(type = "uuid-char")
    @Column(name = "artistID", nullable = false)
    private UUID artistID;

    @NotBlank
    @Size(min = 3, max = 30)
    @Column(name = "artist_name", nullable = false)
    private String artistName;

    @Column
    private String image;

    @Enumerated(EnumType.STRING)
    @Column(name = "artist_type", nullable = false)
    private ArtistType artistType;

    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
    @JoinColumn(name = "accountID", referencedColumnName = "accountID", unique = true)
    @OneToOne(cascade = CascadeType.DETACH)
    private Account account;

    @Size(max = 200)
    private String description;

}
