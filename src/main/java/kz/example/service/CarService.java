package kz.example.service;

import kz.example.dto.CarDTO;
import kz.example.entity.Car;
import kz.example.mapper.CarMapper;
import kz.example.repository.CarRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class CarService {

    private final CarRepository carRepository;

    public CarService(CarRepository carRepository) {
        this.carRepository = carRepository;
    }

    public List<CarDTO> findAll() {
        List<Car> cars = carRepository.findAll();
        return cars.stream().map(CarMapper::toDTO).toList();
    }

    public CarDTO findById(UUID id) {
        Optional<Car> optionalCar = carRepository.findById(id);
        return optionalCar.map(CarMapper::toDTO).orElseThrow(() -> new RuntimeException("Car with id " + id + " not found"));
    }

    public CarDTO save(CarDTO carDTO) {
        Car car = CarMapper.toEntity(carDTO);
        car = carRepository.save(car);
        return CarMapper.toDTO(car);
    }

    public CarDTO update(UUID id, CarDTO carDTO) {
        Optional<Car> optionalCar = carRepository.findById(id);
        if (optionalCar.isPresent()) {
            Car carToUpdate = optionalCar.get();
            carToUpdate.setMake(carDTO.getMake());
            carToUpdate.setModel(carDTO.getModel());
            carToUpdate.setYear(carDTO.getYear());
            carToUpdate.setPrice(carDTO.getPrice());
            carToUpdate.setVin(carDTO.getVin());
            return CarMapper.toDTO(carRepository.save(carToUpdate));
        }
        throw new RuntimeException("Car with id " + id + " not found");
    }

    public void delete(UUID id) {
        carRepository.deleteById(id);
    }

    public List<CarDTO> findByModel(String model) {
        return carRepository.findAllByModel(model).stream().map(CarMapper::toDTO).toList();
    }

    public List<CarDTO> findByYear(int year) {
        return carRepository.findAllByYear(year).stream().map(CarMapper::toDTO).toList();
    }

}
