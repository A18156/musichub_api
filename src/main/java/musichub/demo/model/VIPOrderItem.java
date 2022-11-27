package musichub.demo.model;

import org.hibernate.annotations.Where;

import javax.persistence.DiscriminatorValue;
import javax.persistence.Entity;

@Entity
@DiscriminatorValue("1")
@Where(clause = "order_type = \"1\"")
public class SongOrderItem extends OrderItem {
}
