package org.example.controller;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.*;
import org.example.dao.JobDAO;
import org.example.dto.JobDto;
import org.example.dto.JobFilterDto;
import org.example.exceptions.DataNotFoundException;
import org.example.mappers.JobMapper;
import org.example.models.job;

import java.net.URI;

import java.net.URL;
import java.sql.SQLException;
import java.util.ArrayList;


@Path("/jobs")
public class JobController {

    JobDAO dao = new JobDAO();


    @Context UriInfo uriInfo;
    @Context HttpHeaders headers;

        @GET
        @Produces({MediaType.APPLICATION_XML, MediaType.APPLICATION_JSON,"text/csv"})
        public Response getAllJobs(
                @BeanParam JobFilterDto filter
                ) {

            try {
                GenericEntity<ArrayList<job>> jobs = new GenericEntity<ArrayList<job>>(dao.selectAllJobs(filter)) {};
                if(headers.getAcceptableMediaTypes().contains(MediaType.valueOf(MediaType.APPLICATION_XML))) {
                    return Response
                            .ok(jobs)
                            .type(MediaType.APPLICATION_XML)
                            .build();
//
                }else if(headers.getAcceptableMediaTypes().contains(MediaType.valueOf("text/csv"))) {
                    return Response
                            .ok(jobs)
                            .type("text/csv")
                            .build();
                }

                return Response
//                    .ok()
//                    .entity(jobs)
//                    .type(MediaType.APPLICATION_JSON)
                        .ok(jobs, MediaType.APPLICATION_JSON)
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @GET
        @Path("{job_id}")
        @Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, "text/csv"})

        public Response getJob(
                @PathParam("job_id") int job_id) {

            try {
                job jobs = dao.selectJob(job_id);
                if (jobs == null) {
                    throw new DataNotFoundException("Job " + job_id + "Not found");
                }
                else if(headers.getAcceptableMediaTypes().contains(MediaType.valueOf(MediaType.APPLICATION_XML))) {
                    return Response
                            .ok(jobs)
                            .type(MediaType.APPLICATION_XML)
                            .build();
//
                }

//                JobDto dto = new JobDto();
//                dto.setJob_id(jobs.getJob_id());
//                dto.setJob_title(jobs.getJob_title());
//                dto.setMin_salary(jobs.getMin_salary());
//                dto.setMax_salary(jobs.getMax_salary());
                JobDto dto = JobMapper.INSTANCE.toJobDto(jobs);
              addLinks(dto);

                return Response.ok(dto).build();
              // return jobs;
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        private void addLinks (JobDto dto){
            URI selfUri = uriInfo.getAbsolutePath();
            URI empsUri = uriInfo.getAbsolutePathBuilder().path(JobController.class).build();

            dto.addLink(selfUri.toString(), "self");
            dto.addLink(empsUri.toString(),"employees");
        }

        @DELETE
        @Path("{job_id}")
        public void deleteJob(@PathParam("job_id") int job_id) {

            try {
                dao.deleteJob(job_id);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }

        @POST
       @Consumes({MediaType.APPLICATION_XML,MediaType.APPLICATION_JSON})
        public Response insertJob(JobDto dto) {

            try {
                job jobs = JobMapper.INSTANCE.toModel(dto);
                dao.insertJob(jobs);
                NewCookie cookie = (new NewCookie.Builder("username")).value("00000").build();
                URI uri = uriInfo.getAbsolutePathBuilder().path(jobs.getJob_id()+"").build();
                return Response
                        .created(uri)
                        .cookie(cookie)
                        .header("Created by", "Ragad")
                        .build();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }


//    @POST
//    public Response insertJobsFromForm(@BeanParam job jobs) {
//
//        try {
//            dao.insertJob(jobs);
//            NewCookie cookie = (new NewCookie.Builder("username")).value("OOOOO").build();
//            URI uri = uriInfo.getAbsolutePathBuilder().path(jobs.getJob_id() + "").build();
//            return Response
//                    .created(uri)
//                    .cookie(cookie)
//                    .header("Created by", "Ragad")
//                    .build();
//        } catch (Exception e) {
//            throw new RuntimeException(e);
//        }
//    }

        @PUT
        @Path("{job_id}")
        public void updateJob(@PathParam("job_id") int job_id, job jobs) {

            try {
                jobs.setJob_id(job_id);
                dao.updateJob(jobs);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }



}
