package com.pluralsight.sneakerdrops.service;

import com.pluralsight.sneakerdrops.data.BrandRepository;
import com.pluralsight.sneakerdrops.data.SneakerRepository;
import com.pluralsight.sneakerdrops.models.Brand;
import com.pluralsight.sneakerdrops.models.Sneaker;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SneakerService {

    private final SneakerRepository sneakerRepository;
    private final BrandRepository brandRepository;

    @Autowired
    public SneakerService(SneakerRepository sneakerRepository, BrandRepository brandRepository) {
        this.sneakerRepository = sneakerRepository;
        this.brandRepository = brandRepository;
    }

    public long count() {
        return sneakerRepository.count();
    }

    public List<Sneaker> allSneakers() {
        return sneakerRepository.findAll();
    }

    public List<Brand> allBrands() {
        return brandRepository.findAll();
    }

    public List<Sneaker> byYear(int year) {
        return sneakerRepository.findByReleaseYear(year);
    }

    public List<Sneaker> byModel(String text) {
        return sneakerRepository.findByModelContaining(text);
    }

    public List<Sneaker> byMaxPrice(double price) {
        return sneakerRepository.findByPriceLessThan(price);
    }

    public List<Sneaker> search(double maxPrice, int minYear) {
        return sneakerRepository.search(maxPrice, minYear);
    }

    public List<Sneaker> byBrand(String brandName) {
        return sneakerRepository.findByBrand_Name(brandName);
    }

    public Sneaker byId(long id) {
        return sneakerRepository.findById(id).orElse(null);
    }

    public List<Sneaker> search(Integer year, String model, String brand, Double minPrice, Double maxPrice, String sort) {
        return sneakerRepository.findAll().stream()
                .filter(s -> year == null || s.getReleaseYear() == year)
                .filter(s -> model == null || s.getModel().toLowerCase().contains(model.toLowerCase()))
                .filter(s -> brand == null || s.getBrand() != null && s.getBrand().getName().equalsIgnoreCase(brand))
                .filter(s -> minPrice == null || s.getPrice() >= minPrice)
                .filter(s -> maxPrice == null || s.getPrice() <= maxPrice)
                .toList();

    }
//
//    public Sneaker addSneaker(String model, double price, int year, long brandId) {
//        Brand brand = brandRepository.findById(brandId)
//                .orElseThrow(() -> new NotFoundException("No brand with id " + brandId));
//        return sneakerRepository.save(new Sneaker(model, price, year, brand));
//    }

    public Sneaker createSneaker(Sneaker sneaker) {
        sneaker.setId(null);
        sneaker.setBrand(resolveBrand(sneaker));
        return sneakerRepository.save(sneaker);
    }

//    public Sneaker updatePrice(long id, double price) {
//        Sneaker sneaker = byId(id);
//        sneaker.setPrice(price);
//        return sneakerRepository.save(sneaker);
//    }

    public Sneaker updateSneaker(long id, Sneaker updated) {
        Sneaker existing = byId(id);
        if (existing == null)
            return null;

        existing.setModel(updated.getModel());
        existing.setPrice(updated.getPrice());
        existing.setReleaseYear(updated.getReleaseYear());
        existing.setBrand(resolveBrand(updated));

        return sneakerRepository.save(existing);
    }

//    public void deleteSneaker(long id) {
//        if (!sneakerRepository.existsById(id)) {
//            throw new NotFoundException("No sneaker with id " + id);
//        }
//        sneakerRepository.deleteById(id);
//    }

    public void deleteSneaker(long id){
        sneakerRepository.deleteById(id);
    }

    private Brand resolveBrand(Sneaker sneaker) {
        if (sneaker.getBrand() == null || sneaker.getBrand().getId() == null) return null;
        return brandRepository.findById(sneaker.getBrand().getId()).orElse(null);
    }
}