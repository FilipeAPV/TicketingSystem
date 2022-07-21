package com.mycompany.ticketingsystem.utility;

import org.springframework.stereotype.Component;

public class FilterDTO {
    private boolean open = false;
    private boolean high = false;
    private boolean assigned = false;

    public boolean isOpen() {
        return open;
    }

    public void setOpen(boolean open) {
        this.open = open;
    }

    public boolean isHigh() {
        return high;
    }

    public void setHigh(boolean high) {
        this.high = high;
    }

    public boolean isAssigned() {
        return assigned;
    }

    public void setAssigned(boolean assigned) {
        this.assigned = assigned;
    }
}
