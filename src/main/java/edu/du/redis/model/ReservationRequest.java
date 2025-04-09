package edu.du.redis.model;

import edu.du.redis.dto.Reservation;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

@Data
@Getter
@Setter
public class ReservationRequest {
    private Reservation reservation;
}
