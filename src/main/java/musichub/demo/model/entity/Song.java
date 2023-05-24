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
    @Column(nullable = true, columnDefinition = "TEXT")
    private String title;

    @Column(name = "dateupload",nullable = false)
    private Date dateUpload;

    @Column(nullable = true, columnDefinition = "TEXT")
    private String image;

    @NotNull
    @Column(nullable = true, columnDefinition = "TEXT")
    private String audio;

    @NotNull
    @Column
    private Double price;

    @NotNull
    @Column
    private Boolean isPublic;

    @NotNull
    @Column
    private Integer state;

    //    @JsonInclude(content = JsonInclude.Include.NON_EMPTY)
//    @ManyToOne(fetch = FetchType.LAZY, optional = false)
//    @JoinColumn(name = "accountID", referencedColumnName = "accountID", unique = true)
////    @JoinColumn(name = "accountid", nullable = false)
//    @OnDelete(action = OnDeleteAction.CASCADE)
////    @JsonIgnore
//    private Account accountid;
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "accountID", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Account accountid;

    //    @ManyToMany(fetch = FetchType.LAZY)
//    @JoinTable(name = "type_of_song",
//            joinColumns = @JoinColumn(name = "songid"),
//            inverseJoinColumns = @JoinColumn(name = "songtypeid"))
//    private Set<SongType> songType= new HashSet<>();
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "songtype", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private SongType songType;
}
