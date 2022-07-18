package com.mycompany.ticketingsystem.utility;

import com.mycompany.ticketingsystem.dto.TicketDTO;
import com.mycompany.ticketingsystem.dto.UserDTO;
import com.mycompany.ticketingsystem.model.Ticket;
import com.mycompany.ticketingsystem.model.User;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
public class ConvertListDTO {

    private final ModelMapper modelMapper;

    @Autowired
    public ConvertListDTO(ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
    }

    public List<TicketDTO> convertToDTO(List<Ticket> source) {
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
}
