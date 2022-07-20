package com.mycompany.ticketingsystem.utility;

import org.springframework.stereotype.Component;

@Component
public class FilterDTO {
    private boolean open = false;
    private boolean high = false;

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
}
