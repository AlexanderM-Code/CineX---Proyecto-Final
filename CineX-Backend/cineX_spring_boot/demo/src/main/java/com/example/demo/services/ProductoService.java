package com.example.demo.services;

import com.example.demo.entities.Producto;
import com.example.demo.repositories.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    public Producto create(Producto producto) {
        return productoRepository.save(producto);
    }

    public Producto findById(Long id) {
        return productoRepository.findById(id).orElseThrow(() -> new RuntimeException("Producto no encontrado"));
    }

    public List<Producto> findAll() {
        return productoRepository.findAll();
    }

    public Producto update(Long id, Producto producto) {
        Producto existingProducto = findById(id);
        existingProducto.setNombre(producto.getNombre());
        existingProducto.setPrecio(producto.getPrecio());
        return productoRepository.save(existingProducto);
    }

    public void delete(Long id) {
        productoRepository.deleteById(id);
    }
}
