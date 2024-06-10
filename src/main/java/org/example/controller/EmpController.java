package org.example.controller;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.dao.EmployeeDAO;
import org.example.dto.EmployeeDto;
import org.example.dto.EmployeeFilterDto;
import org.example.dto.JobDto;
import org.example.exceptions.DataNotFoundException;
import org.example.mappers.EmployeeMapper;
import org.example.models.Employee;
import org.example.models.job;


import java.net.URI;
import java.sql.SQLException;
import java.util.ArrayList;

@Path("/employees")
public class EmpController {

        EmployeeDAO dao = new EmployeeDAO();
    @Context UriInfo uriInfo;
    @Context HttpHeaders headers;

        @GET
        @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,"text/csv"})
        public Response getAllEmployee(
                @BeanParam EmployeeFilterDto filter
        ) {

            try {
                GenericEntity<ArrayList<Employee>> emps = new GenericEntity<ArrayList<Employee>>(dao.selectAllEmps(filter)) {};
                if(headers.getAcceptableMediaTypes().contains(MediaType.valueOf(MediaType.APPLICATION_XML))) {
                    return Response
                            .ok(emps)
                            .type(MediaType.APPLICATION_XML)
                            .build();

              //  return dao.selectAllJobs(minsal, limit, offset);
                //return dao.selectAllEmps(filter);
                //return dao.selectAllEmps();
                  }
                else if(headers.getAcceptableMediaTypes().contains(MediaType.valueOf("text/csv"))) {
                    return Response
                            .ok(emps)
                            .type("text/csv")
                            .build();
                }

                return Response
                        .ok(emps, MediaType.APPLICATION_JSON)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @GET
        @Path("{employee_id}")
        @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "text/csv"} )
        public Response getEmployee(
                @PathParam("employee_id") int employee_id) throws SQLException{

            try {
                Employee emps = dao.selectEmp(employee_id);


                if (emps == null) {
                    throw new DataNotFoundException("Employee " + employee_id + " Not found");

                }

                if(headers.getAcceptableMediaTypes().contains(MediaType.valueOf(MediaType.APPLICATION_XML))) {
                    return Response
                            .ok(emps)
                            .type(MediaType.APPLICATION_XML)
                            .build();
                } // return dao.selectEmp(employee_id);

//                EmployeeDto dto = new EmployeeDto();
//                dto.setEmployee_id(emps.getEmployee_id());
//                dto.setFirst_name(emps.getFirst_name());
//                dto.setLast_name(emps.getLast_name());
//                dto.setEmail(emps.getEmail());
//                dto.setNumber(emps.getNumber());
//                dto.setHire_date(emps.getHire_date());
//                dto.setJob_id(emps.getJob_id());
//                dto.setSalary(emps.getSalary());
//                dto.setManager_id(emps.getManager_id());
//                dto.setDepartment_id(emps.getDepartment_id());
                EmployeeDto dto = EmployeeMapper.INSTANCE.toEmpDto(emps);

                addLinks(dto);

                return Response.ok(dto).build();
            }
            catch (ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }



    private void addLinks (EmployeeDto dto){
        URI selfUri = uriInfo.getAbsolutePath();
        URI empsUri = uriInfo.getAbsolutePathBuilder().path(EmpController.class).build();

        dto.addLink(selfUri.toString(), "self");
        dto.addLink(empsUri.toString(),"jobs");
    }

        @DELETE
        @Path("{employee_id}")
        public Response deleteEmployee(
                @PathParam("employee_id") int employee_id) {

            try {
                dao.deleteEmp(employee_id);
                if(headers.getAcceptableMediaTypes().contains(MediaType.valueOf(MediaType.APPLICATION_XML))) {
                    return Response
                            .ok()
                            .type(MediaType.APPLICATION_XML)
                            .build();
//
                }
                return Response.ok().build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @POST
        @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
        public Response insertEmployee(Employee emps) {

            try {
                dao.insertEmp(emps);
                NewCookie cookie = (new NewCookie.Builder("username")).value("00000").build();
                URI uri = uriInfo.getAbsolutePathBuilder().path(emps.getEmployee_id()+"").build();
                return Response
                        .created(uri)
                        .cookie(cookie)
                        .header("Created by", "Ragad Alghanim")
                        .build();
               // dao.insertEmp(emps);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @PUT
        @Path("{employee_id}")
        @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "text/csv"})
        public Response updateEmployee(
                @PathParam("employee_id") int employee_id, Employee emps) {

            try {
                emps.setEmployee_id(employee_id);
                dao.updateEmp(emps);
                if(headers.getAcceptableMediaTypes().contains(MediaType.valueOf(MediaType.APPLICATION_XML))) {
                    return Response
                            .ok(emps)
                            .type(MediaType.APPLICATION_XML)
                            .build();
//
                }
                return Response.ok(emps).build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }



}
