package group.artifact.entities;

import javax.persistence.*;

import lombok.*;

@Data
@Entity
@NoArgsConstructor
@RequiredArgsConstructor
@Table(name = "messages", schema = "project") // mapping to postgres
public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int messageID;

    @NonNull
    @ManyToOne
    @JoinColumn(name = "chat_id")
    private PostBox postBoxID;

    @NonNull
    @Column(name = "sender_id")
    private int sender;

    @NonNull
    @Column(name = "recipient_id")
    private int recipient;

    @NonNull
    private String subject;

    @NonNull
    private String content;

    @NonNull
    private boolean read;


}
