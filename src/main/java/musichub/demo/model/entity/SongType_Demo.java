package musichub.demo.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "songtype_demo")
public class SongType_Demo extends BaseEntityUUID {
    @Column(name = "name", nullable = false)
    private String name;
}
