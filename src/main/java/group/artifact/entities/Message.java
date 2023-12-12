package group.artifact.entities;

import java.time.ZonedDateTime;
import java.util.Set;

import javax.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Table(name = "messages", schema = "project") // mapping to postgres
public class Message {

    private int messageID;

    private int senderID;

    private int recipientID;

    private String subject;

    private String content;

    private boolean read;
}
