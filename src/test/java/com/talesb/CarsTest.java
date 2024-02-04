package com.talesb;

import com.talesb.entity.Car;
import com.talesb.entity.CarDTO;
import com.talesb.exception.BusinessException;
import com.talesb.repository.CarRepository;
import com.talesb.service.CarService;
import io.quarkus.test.InjectMock;
import io.quarkus.test.common.QuarkusTestResource;
import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Optional;


class CarsTest {


    @InjectMocks
    private CarService carsService;

    @Mock
    private CarRepository carRepository;

    @Captor
    ArgumentCaptor<Car> carArgumentCaptor;

    @BeforeEach
    void init_mocks() {
        MockitoAnnotations.initMocks(this);
    }


    @Test
    public void findAllTest() {
        Car carToBeTested1 = new Car(1L, "BMW", LocalDate.of(2021, 1, 1), "M3", "Black", 1000., 100000.);
        Car carToBeTested2 = new Car(2L, "Audi", LocalDate.of(2020, 2, 1), "A3", "White", 1000., 10000.);
        Mockito.when(carRepository.listAll()).thenReturn(Arrays.asList(carToBeTested1, carToBeTested2));
        Assertions.assertEquals(2, carsService.findAll().size());
        Assertions.assertEquals("BMW", carsService.findAll().get(0).getBrand());
        Assertions.assertEquals("M3", carsService.findAll().get(0).getModel());
        Assertions.assertEquals("Audi", carsService.findAll().get(1).getBrand());
        Assertions.assertEquals("A3", carsService.findAll().get(1).getModel());
        //...
    }


    @Test
    public void findByIdTest() {
        Car carToBeTested1 = new Car(1L, "BMW", LocalDate.of(2021, 1, 1), "M3", "Black", 1000., 100000.);
        Mockito.when(carRepository.findByIdOptional(1L)).thenReturn(Optional.of(carToBeTested1));
        CarDTO result = carsService.findById(1L).get();
        Assertions.assertEquals("BMW", result.getBrand());
        Assertions.assertEquals("M3", result.getModel());
        //...
    }

    @Test
    public void findByIdEmptyTest() {
        Optional<CarDTO> result = carsService.findById(1L);
        Assertions.assertEquals(Optional.empty(), result);
    }

    @Test
    public void createTest() {

        CarDTO carToBeTested = new CarDTO("BMW", LocalDate.of(2021, 1, 1), "M3", "Black", 1000., 100000.);
        carsService.create(carToBeTested);
        Mockito.verify(carRepository, Mockito.times(1)).persist(carArgumentCaptor.capture());


        Assertions.assertEquals("BMW", carArgumentCaptor.getValue().getBrand());
        Assertions.assertEquals("M3", carArgumentCaptor.getValue().getModel());
        //...

    }

    @Test
    public void createTestWithBrandError() {

        CarDTO carToBeTested = new CarDTO("Fiat", LocalDate.of(2021, 1, 1), "500", "Red", 1000., 100000.);
        Assertions.assertThrows(BusinessException.class, () -> carsService.create(carToBeTested));

    }


    @Test
    public void createTestWithKilometersError() {

        CarDTO carToBeTested = new CarDTO("VOLKSWAGEN", LocalDate.of(2021, 1, 1), "Polo", "Gray", 999999999., 100000.);
        Assertions.assertThrows(BusinessException.class, () -> carsService.create(carToBeTested));

    }

    @Test
    public void updateTest() {

        Car carRetrieved = new Car(1L, "BMW", LocalDate.of(2021, 1, 1), "M3", "Black", 1000., 100000.);
        Mockito.when(carRepository.findByIdOptional(1L)).thenReturn(Optional.of(carRetrieved));

        CarDTO carToBeTested = new CarDTO("BMW", LocalDate.of(2021, 1, 1), "116", "Gray", 1000., 100000.);

        carsService.update(1L, carToBeTested);

        Mockito.verify(carRepository, Mockito.times(1)).persist(carArgumentCaptor.capture());

        Assertions.assertEquals("BMW", carArgumentCaptor.getValue().getBrand());
        Assertions.assertEquals("116", carArgumentCaptor.getValue().getModel());
        Assertions.assertEquals("Gray", carArgumentCaptor.getValue().getColor());
        //...

    }


    @Test
    public void deleteTest() {
        carsService.delete(1L);
        Mockito.verify(carRepository, Mockito.times(1)).deleteById(1L);
    }
}