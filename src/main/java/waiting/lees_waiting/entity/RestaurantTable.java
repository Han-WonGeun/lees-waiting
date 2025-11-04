package waiting.lees_waiting.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Entity
@Getter
@Setter
public class RestaurantTable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private int capacity; // 2 or 4

    private LocalTime entryTime; // Add this field to store the actual entry time
    private LocalDateTime estimatedEndTime;
}
