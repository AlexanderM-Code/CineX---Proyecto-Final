package com.example.demo.controllers;

import com.example.demo.entities.Producto;
import com.example.demo.services.ProductoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/productos")
@Tag(name = "Productos", description = "Productos de confitería")
public class ProductoController {

    private final ProductoService productoService;

    public ProductoController(ProductoService productoService) {
        this.productoService = productoService;
    }

    @GetMapping
    @Operation(summary = "Listar productos")
    public List<Producto> findAll() {
        return productoService.findAll();
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener por id")
    public Producto findById(@PathVariable Long id) {
        return productoService.findById(id);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear")
    public Producto create(@Valid @RequestBody Producto producto) {
        return productoService.create(producto);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Actualizar")
    public Producto update(@PathVariable Long id, @Valid @RequestBody Producto producto) {
        return productoService.update(id, producto);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar")
    public void delete(@PathVariable Long id) {
        productoService.delete(id);
    }
}
