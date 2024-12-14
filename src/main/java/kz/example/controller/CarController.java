package kz.example.controller;

import kz.example.dto.CarDTO;
import kz.example.service.CarService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api")
public class CarController {

    private final CarService carService;

    public CarController(CarService carService) {
        this.carService = carService;
    }

    @GetMapping("/cars")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CarDTO>> getCars() {
        return ResponseEntity.ok(carService.findAll());
    }

    @GetMapping("/cars/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<CarDTO> getCar(@PathVariable UUID id) {
        return ResponseEntity.ok(carService.findById(id));
    }

    @PostMapping("/cars")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarDTO> createCar(@RequestBody CarDTO carDTO) {
        return ResponseEntity.ok(carService.save(carDTO));
    }

    @PutMapping("/cars/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<CarDTO> updateCar(@PathVariable UUID id, @RequestBody CarDTO carDTO) {
        return ResponseEntity.ok(carService.update(id, carDTO));
    }

    @DeleteMapping("/cars/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteCar(@PathVariable UUID id) {
        carService.delete(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/cars/model/{model}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CarDTO>> getCarsByModel(@PathVariable String model) {
        List<CarDTO> carDTOList = carService.findByModel(model);
        return ResponseEntity.ok(carDTOList);
    }

    @GetMapping("/cars/year/{year}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<List<CarDTO>> getCarsByYear(@PathVariable int year) {
        List<CarDTO> carDTOList = carService.findByYear(year);
        return ResponseEntity.ok(carDTOList);
    }

}
