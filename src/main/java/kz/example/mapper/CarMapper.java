package kz.example.mapper;

import kz.example.dto.CarDTO;
import kz.example.entity.Car;

public class CarMapper {

    public static CarDTO toDTO(Car car) {
        CarDTO carDTO =  new CarDTO();
        carDTO.setId(car.getId());
        carDTO.setMake(car.getMake());
        carDTO.setModel(car.getModel());
        carDTO.setYear(car.getYear());
        carDTO.setPrice(car.getPrice());
        carDTO.setVin(car.getVin());
        return carDTO;
    }

    public static Car toEntity(CarDTO carDTO) {
        Car car = new Car();
        car.setId(carDTO.getId());
        car.setMake(carDTO.getMake());
        car.setModel(carDTO.getModel());
        car.setYear(carDTO.getYear());
        car.setPrice(carDTO.getPrice());
        car.setVin(carDTO.getVin());
        return car;
    }
}
