package org.bmc.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.bmc.model.PassengerInfo;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PassengerInfoDTO {
    private Long passengerId;
    private Integer survived;
    private Integer pClass;
    private String name;
    private String sex;
    private Double age;
    private Integer sibSb;
    private Integer parch;
    private String ticket;
    private Double fare;
    private String cabin;
    private String embarked;

    public static PassengerInfoDTO mapPassengerInfoToPassengerInfoDTO(PassengerInfo passengerInfo) {
        return PassengerInfoDTO.builder()
                .passengerId(passengerInfo.getPassengerId())
                .survived(passengerInfo.getSurvived())
                .pClass(passengerInfo.getPClass())
                .name(passengerInfo.getName())
                .sex(passengerInfo.getSex())
                .age(passengerInfo.getAge())
                .sibSb(passengerInfo.getSibSb())
                .parch(passengerInfo.getParch())
                .ticket(passengerInfo.getTicket())
                .fare(passengerInfo.getFare())
                .cabin(passengerInfo.getCabin())
                .embarked(passengerInfo.getEmbarked())
                .build();
    }
}