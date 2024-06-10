package org.example.mappers;

import org.example.dto.JobDto;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;
import org.example.models.job;


@Mapper
public interface JobMapper {
    JobMapper INSTANCE = Mappers.getMapper(JobMapper.class);


//    @Mapping(source = "departmentId", target = "deptId")
//    @Mapping(source = "departmentName", target = "deptName")
//    @Mapping(source = "locationId", target = "locId")
    JobDto toJobDto(job j);

//    @Mapping(target = "departmentId", source = "deptId")
//    @Mapping(target = "departmentName", source = "deptName")
//    @Mapping(target = "locationId", source = "locId")
    job toModel(JobDto dto);
}
