package com.mycompany.ticketingsystem.utility;

import com.mycompany.ticketingsystem.model.Ticket;
import org.springframework.data.jpa.domain.Specification;

import java.text.MessageFormat;


public final class DbFilterSpecification {

    public static Specification<Ticket> statusContains(String expression) {
        return (root, query, builder) -> builder.like(root.get("status"), contains(expression));
    }

    public static Specification<Ticket> priorityContains(String expression) {
        return (root, query, builder) -> builder.like(root.get("priority"), contains(expression));
    }

    private static String contains(String expression) {
        return MessageFormat.format("%{0}%", expression);
    }

}
