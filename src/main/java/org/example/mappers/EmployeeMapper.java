package org.example.mappers;

import org.example.dto.EmployeeDto;
import org.example.dto.JobDto;
import org.example.models.Employee;
import org.example.models.job;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;


@Mapper(uses = {JobMapper.class},imports = {java.util.UUID.class})
public interface EmployeeMapper {
    EmployeeMapper INSTANCE = Mappers.getMapper(EmployeeMapper.class);

    @Mapping(source = "jobb", target = "job_Dto")
    EmployeeDto toEmpDto(Employee e);

    @Mapping(source = "e.job_id", target = "job_id")
    EmployeeDto toEmpDto(Employee e, job j);

   // EmployeeDto toEmpDto(job j);
   @Mapping(target = "jobb", source = "job_Dto")
    Employee toModel(EmployeeDto dto);
}
