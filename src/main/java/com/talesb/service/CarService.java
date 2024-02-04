package com.talesb.service;

import com.talesb.entity.Car;
import com.talesb.entity.CarDTO;
import com.talesb.entity.GermanCarBrand;
import com.talesb.exception.BusinessException;
import com.talesb.repository.CarRepository;
import jakarta.enterprise.context.RequestScoped;
import jakarta.inject.Inject;
import jakarta.transaction.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@RequestScoped
@Transactional
public class CarService {

    @Inject
    CarRepository carRepository;


    public List<Car> findAll() {
        return carRepository.listAll();
    }

    public Optional<CarDTO> findById(Long id) {

        Optional<Car> car = carRepository.findByIdOptional(id);
        if (car.isPresent()) {
            CarDTO carDTO = Car.toDTO(car.get());
            return Optional.of(carDTO);
        }
        return Optional.empty();
    }

    public void create(CarDTO car) {
        applyValidations(car);
        Car carToPersist = Car.fromDTO(car);
        carRepository.persist(carToPersist);
    }

    private void applyValidations(CarDTO car) {
        if (car.getKilometers() > 999999) {
            throw new BusinessException("Kilometers cannot be greater than 999999");
        }
        boolean germanCar = Arrays.stream(GermanCarBrand.values()).anyMatch(germanCarBrand -> germanCarBrand.name().equals(car.getBrand()));

        if (!germanCar) {
            throw new BusinessException("Brand is not a German car brand");
        }
    }

    public void update(Long id, CarDTO car) {
        applyValidations(car);
        Optional<Car> carToUpdate = this.carRepository.findByIdOptional(id);

        if (carToUpdate.isEmpty()) {
            throw new BusinessException("Car not found");
        }

        carToUpdate.get().setBrand(car.getBrand());
        carToUpdate.get().setKilometers(car.getKilometers());
        carToUpdate.get().setColor(car.getColor());
        carToUpdate.get().setModel(car.getModel());
        carToUpdate.get().setPrice(car.getPrice());
        carToUpdate.get().setReleaseDate(car.getReleaseDate());
        carRepository.persist(carToUpdate.get());
    }

    public void delete(Long id) {
        carRepository.deleteById(id);
    }


}
