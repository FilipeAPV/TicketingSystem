package com.mycompany.ticketingsystem.utility;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import java.util.ArrayList;
import java.util.List;

@Component
public class ConvertListDTO {

    private final ModelMapper modelMapper;

    @Autowired
    public ConvertListDTO(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public <T, C> List<T> convertListToListDTO(List<C> listToConvert, Class<T> classNameOfTheReturnListType) {
        List<T> listDTO = new ArrayList<>();
        for (C val : listToConvert) {
            listDTO.add(modelMapper.map(val, classNameOfTheReturnListType));
        }
        return listDTO;
    }

}
