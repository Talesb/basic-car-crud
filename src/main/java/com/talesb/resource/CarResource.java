package com.talesb.resource;


import com.talesb.entity.Car;
import com.talesb.entity.CarDTO;
import com.talesb.service.CarService;
import jakarta.inject.Inject;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import org.jboss.resteasy.reactive.RestPath;

import java.util.List;


@Path("/cars")
public class CarResource {

    @Inject
    CarService carService;


    @GET
    public List<Car> getAllCars() {
        return carService.findAll();
    }

    @GET
    @Path("/{id}")
    public CarDTO getCarById(@RestPath Long id) {
        return carService.findById(id);
    }

    @PUT
    @Path("/{id}")
    public void updateCar(@RestPath Long id, Car car) {
        carService.update(car);
    }

    @POST
    @Consumes("application/json")
    public Response createCar(CarDTO car) {
        carService.create(Car.fromDTO(car));
        return Response.ok().status(201).build();
    }

    @DELETE
    @Path("/{id}")
    public void deleteCar(@RestPath Long id) {
        carService.delete(id);
    }




}
