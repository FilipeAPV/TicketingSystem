package com.mycompany.ticketingsystem.audit;

import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("auditAwareImpl")
public class AuditAwareImpl {
/*    @Override
    public Optional<String> getCurrentAuditor() {
        return Optional.ofNullable(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());
    }
    // This Bean will identify which user is performing the operation inside the DB
    // If the user that performs the modification is not authenticated, then the value will be null.*/
}
