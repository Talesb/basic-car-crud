package com.talesb.service;

import com.talesb.entity.Car;
import com.talesb.entity.CarDTO;
import com.talesb.repository.CarRepository;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.List;

@RequestScoped
@Transactional
public class CarService {

    @Inject
    CarRepository carRepository;


    public List<Car> findAll() {
        return carRepository.listAll();
    }

    public CarDTO findById(Long id) {

       Car car = carRepository.findById(id);

       if (car != null) {
              return Car.toDTO(car);

         } else {
              return null;
         }
    }

    public void create(Car car) {
        carRepository.persist(car);
    }

    public void update(Car car) {
        carRepository.persist(car);
    }

    public void delete(Long id) {
        carRepository.deleteById(id);
    }


}
