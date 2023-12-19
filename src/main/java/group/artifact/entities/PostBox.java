package group.artifact.entities;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@NoArgsConstructor
@Table(name = "postBox", schema = "project")
public class PostBox {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int postBoxID;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
