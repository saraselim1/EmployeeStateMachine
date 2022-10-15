package com.statemachine.core.dal.model;

import com.statemachine.core.converter.EmployeeStatusConverter;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "employee")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @NotNull
    @NotEmpty
    @Column(name = "email")
    private String email;

    @NotNull
    @NotEmpty
    @Column(name = "name")
    private String name;

    @NotNull
    private Integer age;

    @NotNull
    @OneToOne(mappedBy = "employee",fetch = FetchType.EAGER,cascade = CascadeType.PERSIST)
    private Contract contract;

    @NotNull
    @Convert(converter = EmployeeStatusConverter.class)
    @Column(name = "status")
    private List<String> currentStatusList;

}
