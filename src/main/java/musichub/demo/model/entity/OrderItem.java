//package musichub.demo.model;
//
//import lombok.Data;
//import lombok.experimental.Accessors;
//
//import javax.persistence.*;
//
//@Data
//@Accessors(chain = true)
//@Entity
//@Table(name = "order_item")
//@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
//@DiscriminatorColumn(name="order_type", discriminatorType = DiscriminatorType.INTEGER)
//@DiscriminatorValue("0")
//public class OrderItem {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    @Column(name = "id")
//    private Long id;
//    private String name;
//    private long price;
//}
