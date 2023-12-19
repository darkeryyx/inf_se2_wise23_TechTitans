package group.artifact.entities;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Entity
@NoArgsConstructor
@Table(name = "messages", schema = "project") // mapping to postgres
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageID;

    @ManyToOne
    @JoinColumn(name = "chat_id")
    private PostBox postBoxID;

    @Column(name = "sender_id")
    private int sender;


    @Column(name = "recipient_id")
    private int recipient;

    private String subject;

    private String content;

    private boolean read;


}
