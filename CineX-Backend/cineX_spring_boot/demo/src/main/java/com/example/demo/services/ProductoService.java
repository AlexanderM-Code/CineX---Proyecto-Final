package com.example.demo.services;

import com.example.demo.entities.Producto;
import com.example.demo.exceptions.ResourceNotFoundException;
import com.example.demo.repositories.ProductoRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ProductoService {

    private final ProductoRepository productoRepository;

    public ProductoService(ProductoRepository productoRepository) {
        this.productoRepository = productoRepository;
    }

    @Transactional
    public Producto create(Producto producto) {
        producto.setId(null);
        return productoRepository.save(producto);
    }

    public Producto findById(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("Producto", id));
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    @Transactional
    public Producto update(Long id, Producto producto) {
        Producto existente = findById(id);
        existente.setNombre(producto.getNombre());
        existente.setPrecio(producto.getPrecio());
        return productoRepository.save(existente);
    }

    @Transactional
    public void delete(Long id) {
        productoRepository.delete(findById(id));
    }
}
