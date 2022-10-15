package com.statemachine.core.mapper;

import com.statemachine.common.enums.StatusEventEnum;
import com.statemachine.core.dto.payload.request.AddingEmployeeRequest;
import com.statemachine.core.dto.payload.response.EmployeeDetailsResponse;
import com.statemachine.core.dal.model.Employee;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmployeeMapper {

    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(target = "contract.company.id", source = "contract.companyId")
    Employee toEntity(AddingEmployeeRequest addingEmployeeRequest);

    @Mapping(target = "expectedEventList", source = "expectedEventList")
    EmployeeDetailsResponse toResponse(Employee savedEmployee, List<StatusEventEnum> expectedEventList);
}
