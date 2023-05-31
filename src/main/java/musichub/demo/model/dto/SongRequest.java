package musichub.demo.model.dto;

import javax.persistence.Column;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.sql.Date;

public class SongRequest {
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

}
