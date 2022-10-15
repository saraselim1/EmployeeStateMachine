package com.statemachine.core.dal.model;

import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "contract")
public class Contract {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    private Long duration;

    @NotNull
    private LocalDateTime startDate;

    @NotNull
    private Double salary;

    @ManyToOne
    @JoinColumn(name = "company_id")
    private Company company;

    @OneToOne(fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    @JoinColumn(name = "employee_id", referencedColumnName = "id")
    private Employee employee;
}
