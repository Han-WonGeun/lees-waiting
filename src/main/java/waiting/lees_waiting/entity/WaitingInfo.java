package waiting.lees_waiting.entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
public class WaitingInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int people;
    private String phone;

    @JsonFormat(pattern = "HH:mm")
    private LocalDateTime registeredAt;

    @Enumerated(EnumType.STRING)
    private WaitingStatus status;
}
