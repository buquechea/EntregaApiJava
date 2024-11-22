package com.ProyectoFinalJava.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.ProyectoFinalJava.DTO.DisqueriaCreateDTO;
import com.ProyectoFinalJava.DTO.DisqueriaDTO;
import com.ProyectoFinalJava.exceptions.DisqueriaNotFoundException;
import com.ProyectoFinalJava.services.DisqueriaService;
import com.ProyectoFinalJava.utils.ApiResponseMsg;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;

@RestController
@RequestMapping("/api/disquerias")
public class DisqueriaController {

    private final DisqueriaService disqueriaService;

    public DisqueriaController(DisqueriaService disqueriaService) {
        this.disqueriaService = disqueriaService;
    }

    @GetMapping("/all")
    @Operation(summary = "Obtener todas las disquerías", description = "Retorna todas las disquerías.")
    public ResponseEntity<List<DisqueriaDTO>> getAllDisquerias(
            @RequestParam(value = "includeRelations", defaultValue = "false") boolean includeRelations) {
        return ResponseEntity.ok(disqueriaService.getAllDisquerias(includeRelations));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Obtener una disquería por su ID", description = "Retorna la disquería especificada.")
    public ResponseEntity<?> getDisqueriaById(@PathVariable("id") Long id) {
        try {
            DisqueriaDTO disqueria = disqueriaService.getDisqueriaById(id, false);
            return ResponseEntity.ok(disqueria);
        } catch (DisqueriaNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponseMsg("Error", e.getMessage()));
        }
    }

    @PostMapping("/create")
    @Operation(summary = "Crear una disquería", description = "Crea y retorna la nueva disquería.")
    public ResponseEntity<DisqueriaDTO> createDisqueria(@RequestBody DisqueriaCreateDTO disqueriaCreateDTO) {
        DisqueriaDTO createdDisqueria = disqueriaService.saveDisqueria(disqueriaCreateDTO);
        return ResponseEntity.ok(createdDisqueria);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Modificar una disquería", description = "Actualiza y retorna la disquería modificada.")
    public ResponseEntity<DisqueriaDTO> updateDisqueria(@PathVariable Long id,
            @RequestBody DisqueriaCreateDTO disqueriaCreateDTO) {
        DisqueriaDTO updatedDisqueria = disqueriaService.updateDisqueria(id, disqueriaCreateDTO);
        return ResponseEntity.ok(updatedDisqueria);
    }

    @DeleteMapping("/delete/{id}")
    @Operation(summary = "Eliminar una disquería", description = "Elimina una disquería específica.")
    public ResponseEntity<?> deleteDisqueria(@PathVariable("id") Long id) {
        try {
            disqueriaService.deleteDisqueria(id);
            return ResponseEntity.ok().body(new ApiResponseMsg("Disquería eliminada", id));
        } catch (DisqueriaNotFoundException e) {
            return ResponseEntity.badRequest().body(new ApiResponseMsg("Error", e.getMessage()));
        }
    }
}

