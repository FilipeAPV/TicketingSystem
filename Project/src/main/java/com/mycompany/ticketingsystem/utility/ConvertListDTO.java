package com.mycompany.ticketingsystem.utility;

import com.mycompany.ticketingsystem.dto.TicketDTO;
import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class ConvertListDTO {

    private final ModelMapper modelMapper;

    @Autowired
    public ConvertListDTO(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<TicketDTO> convertTicketToDTO(List<Ticket> source) {
        List<TicketDTO> convertedList = new ArrayList<>();
        for (Ticket ticket : source) {
            convertedList.add(modelMapper.map(ticket, TicketDTO.class));
        }
        return convertedList;
    }

    public List<Ticket> convertToModel(List<TicketDTO> source) {
        List<Ticket> convertedList = new ArrayList<>();
        for (TicketDTO ticketDTO: source) {
            convertedList.add(modelMapper.map(ticketDTO, Ticket.class));
        }
        return convertedList;
    }

    public Set<UserDTO> convertUserToDTO(Set<User> userList) {
        Set<UserDTO> userDTOList = new HashSet<>();
        for (User user: userList) {
            userDTOList.add(modelMapper.map(user, UserDTO.class));
        }
        return userDTOList;
    }

    public <T, C> List<T> convertListToListDTO(T returnListType, List<C> listToConvert, Class<T> classNameOfTheReturnListType) {
        List<T> listDTO = new ArrayList<>();
        for (C val : listToConvert) {
            listDTO.add(modelMapper.map(val, classNameOfTheReturnListType));
        }
        return listDTO;
    }

}
