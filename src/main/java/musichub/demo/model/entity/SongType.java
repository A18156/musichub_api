package musichub.demo.model.entity;

import lombok.Data;
import lombok.EqualsAndHashCode;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "songtype")
public class Song_Type extends BaseEntity<Long>{
    @Column(name = "name", nullable = false)
    private String name;
}
