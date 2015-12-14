package com.epam.ticket.controllers;

import com.epam.ticket.dao.EpamTicketDAO.TicketCountsResult;
import com.epam.ticket.data.EpamNewTicket;
import com.epam.ticket.data.EpamTicket;
import com.epam.ticket.facades.EpamTicketSearchCriteria;
import com.epam.ticket.facades.impl.DefaultEpamTicketFacade;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.Serializable;
import java.util.Collection;

import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.web.bind.annotation.RequestMethod.GET;
import static org.springframework.web.bind.annotation.RequestMethod.POST;

@RestController
@RequestMapping("/v1/tickets")
public class EpamTicketController {
    
    @Autowired
    private DefaultEpamTicketFacade defaultEpamTicketFacade;

    @RequestMapping(method = GET)
    public Collection<EpamTicket> getTicketsByCriteria(EpamTicketSearchCriteria searchCriteria) {
        return defaultEpamTicketFacade.getTicketsByCriteria(searchCriteria);
    }

    @RequestMapping(method = POST, consumes = APPLICATION_JSON_VALUE)
    public EpamTicket addTicket(@RequestBody EpamNewTicket ticket) {
        return defaultEpamTicketFacade.addTicket(ticket.getNewTicket(), ticket.getCreationEvent());
    }

    @RequestMapping(value = "/{ticketId}", method = GET)
    public EpamTicket getTicket(@PathVariable("ticketId") String ticketId) {
        return defaultEpamTicketFacade.getTicketById(ticketId);
    }

    @RequestMapping(value = "/ticketCount", method = GET)
    public TicketCounterHolder getTicketCount() {
        TicketCounterHolder ticketCounterHolder = new TicketCounterHolder();
        ticketCounterHolder.setTotal(defaultEpamTicketFacade.getTotalTicketCount());
        return ticketCounterHolder;
    }

    @RequestMapping(value = "/ticketCounts", method = RequestMethod.GET)
    public TicketCountsResult getTicketCounts(@RequestParam(value = "userName", required = false, defaultValue = "csagent") String userName) {
        // TODO: GET RID of userName, when security will be ready!
        return defaultEpamTicketFacade.getTicketCounts(userName);
    }

    private class TicketCounterHolder  implements Serializable{
        private Integer total;

        public Integer getTotal() {
            return total;
        }

        public void setTotal(Integer total) {
            this.total = total;
        }
    }
}
