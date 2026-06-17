package com.pluralsight.sneakerdrops.controllers;

import com.pluralsight.sneakerdrops.models.Sneaker;
import com.pluralsight.sneakerdrops.service.SneakerService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sneakers")
@CrossOrigin
public class SneakerController {

    private final SneakerService sneakerService;

    public SneakerController(SneakerService sneakerService) {
        this.sneakerService = sneakerService;
    }

    @GetMapping
    public List<Sneaker> getAll(@RequestParam(required = false) Integer year,
                                @RequestParam(required = false) String model,
                                @RequestParam(required = false) String brand,
                                @RequestParam(required = false) Double minPrice,
                                @RequestParam(required = false) Double maxPrice,
                                @RequestParam(required = false) String sort) {
        return sneakerService.search(year, model, brand, minPrice, maxPrice, sort);
    }

    @GetMapping("/{id}")
    public Sneaker getById(@PathVariable long sneakerId) {
        return sneakerService.byId(sneakerId);
    }

    @PostMapping
    public ResponseEntity<Sneaker> create(@RequestBody Sneaker sneaker){
        Sneaker sneaker1 = sneakerService.createSneaker(sneaker);
        return ResponseEntity.status(HttpStatus.CREATED).body(sneaker1);
    }

    @PutMapping("/{id}")
    public Sneaker update(@PathVariable long id, @RequestBody Sneaker sneaker) {
        return sneakerService.updateSneaker(id, sneaker);
    }

    @DeleteMapping("{/id}")
    public ResponseEntity<Void> delete(@PathVariable long id) {
        sneakerService.deleteSneaker(id);
        return ResponseEntity.noContent().build();
    }
}
