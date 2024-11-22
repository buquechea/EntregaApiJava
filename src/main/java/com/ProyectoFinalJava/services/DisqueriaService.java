package com.ProyectoFinalJava.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.ProyectoFinalJava.DTO.DisqueriaCreateDTO;
import com.ProyectoFinalJava.DTO.DisqueriaDTO;
import com.ProyectoFinalJava.exceptions.DisqueriaNotFoundException;
import com.ProyectoFinalJava.mapper.DisqueriaMapper;
import com.ProyectoFinalJava.model.Disqueria;
import com.ProyectoFinalJava.repository.DisqueriaRepository;

@Service
public class DisqueriaService {

    private final DisqueriaRepository disqueriaRepository;
    private final DisqueriaMapper disqueriaMapper;

    public DisqueriaService(DisqueriaMapper disqueriaMapper, DisqueriaRepository disqueriaRepository) {
        this.disqueriaMapper = disqueriaMapper;
        this.disqueriaRepository = disqueriaRepository;
    }

    public List<DisqueriaDTO> getAllDisquerias(boolean includeRelations) {
        return disqueriaRepository.findAll().stream()
                .map(disqueria -> disqueriaMapper.toDTODisqueria(disqueria, includeRelations))
                .collect(Collectors.toList());
    }

    public DisqueriaDTO getDisqueriaById(Long id, boolean includeRelations) {
        return disqueriaRepository.findById(id)
                .map(disqueria -> disqueriaMapper.toDTODisqueria(disqueria, includeRelations))
                .orElseThrow(() -> new DisqueriaNotFoundException("Disquería no encontrada con ID: " + id));
    }

    public DisqueriaDTO saveDisqueria(DisqueriaCreateDTO disqueriaCreateDTO) {
        Disqueria disqueria = disqueriaMapper.toEntity(disqueriaCreateDTO);
        Disqueria savedDisqueria = disqueriaRepository.save(disqueria);
        return disqueriaMapper.toDTODisqueria(savedDisqueria, false);
    }

    public DisqueriaDTO updateDisqueria(Long id, DisqueriaCreateDTO disqueriaCreateDTO) {
        return disqueriaRepository.findById(id)
                .map(disqueria -> {
                    disqueria.setNombre(disqueriaCreateDTO.getNombre());
                    disqueria.setDireccion(disqueriaCreateDTO.getDireccion());
                    disqueria.setTelefono(disqueriaCreateDTO.getTelefono());
                    Disqueria updatedDisqueria = disqueriaRepository.save(disqueria);
                    return disqueriaMapper.toDTODisqueria(updatedDisqueria, false);
                })
                .orElseThrow(() -> new DisqueriaNotFoundException("Disquería no encontrada con ID: " + id));
    }

    public void deleteDisqueria(Long id) {
        if (!disqueriaRepository.existsById(id)) {
            throw new DisqueriaNotFoundException("La disquería no existe con ID: " + id);
        }
        disqueriaRepository.deleteById(id);
    }
}
