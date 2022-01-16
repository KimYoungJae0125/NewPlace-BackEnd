package shop.newplace.Account.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Embeddable;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class ProfileId implements Serializable {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long profileId;
    private Long userId;
}
