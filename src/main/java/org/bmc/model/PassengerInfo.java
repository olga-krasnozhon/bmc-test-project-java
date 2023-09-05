package org.bmc.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "passenger_info")
@Data
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Access(value = AccessType.FIELD)
@ToString
public class PassengerInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Column(name = "PassengerId")
    private Long passengerId;

    @Column(name = "Survived")
    private Integer survived;

    @Column(name = "Pclass")
    private Integer pClass;

    @Column(name = "Name")
    private String name;

    //@Enumerated(EnumType.STRING)
    @Column(name = "Sex")
    private String sex;

    @Column(name = "Age")
    private Double age;

    @Column(name = "SibSb")
    private Integer sibSb;

    @Column(name = "Parch")
    private Integer parch;

    @Column(name = "Ticket")
    private String ticket;

    @Column(name = "Fare")
    private Double fare;

    @Column(name = "Cabin")
    private String cabin;

    @Column(name = "Embarked")
    private String embarked;
}