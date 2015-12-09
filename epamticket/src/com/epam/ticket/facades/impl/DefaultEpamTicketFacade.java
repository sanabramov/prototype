package com.epam.ticket.facades.impl;

import com.epam.ticket.converter.CsCustomerEventConverter;
import com.epam.ticket.converter.CsTicketConverter;
import com.epam.ticket.converter.EpamTicketConverter;
import com.epam.ticket.data.EpamCustomerEvent;
import com.epam.ticket.data.EpamTicket;
import com.epam.ticket.facades.EpamTicketFacade;
import com.epam.ticket.facades.EpamTicketSearchCriteria;
import com.epam.ticket.services.EpamTicketService;
import de.hybris.platform.ticket.model.CsTicketModel;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

import static com.google.common.base.Preconditions.checkNotNull;

public class DefaultEpamTicketFacade implements EpamTicketFacade {

    public static final Logger LOG = Logger.getLogger(DefaultEpamTicketFacade.class);

    private EpamTicketConverter ticketConverter;
    private CsTicketConverter csTicketConverter;
    private EpamTicketService ticketService;
    private CsCustomerEventConverter csCustomerEventConverter;

    public DefaultEpamTicketFacade(EpamTicketConverter ticketConverter, CsTicketConverter csTicketConverter,
                                   EpamTicketService ticketService) {
        this.ticketConverter = checkNotNull(ticketConverter);
        this.csTicketConverter = checkNotNull(csTicketConverter);
        this.ticketService = checkNotNull(ticketService);
    }

    @Override
    public void addTicket(EpamTicket ticket, EpamCustomerEvent event) {
        ticketService.addTicket(csTicketConverter.convert(ticket), csCustomerEventConverter.convert(event));
    }

    @Override
    public List<EpamTicket> getTicketsByCriteria(EpamTicketSearchCriteria searchCriteria) {
        LOG.info("Search by criteria: " + searchCriteria);
        List<CsTicketModel> csTicketModels = ticketService.getTicketsByCriteria(searchCriteria);
        return getEpamTickets(csTicketModels);
    }

    @Override
    public EpamTicket getTicketById(String ticketId) {
        LOG.info("Get ticket by id: " + ticketId);
        CsTicketModel csTicketModel = ticketService.getTicketById(ticketId);
        return ticketConverter.convert(csTicketModel);
    }

    @Override
    public Integer getTotalTicketCount() {
        LOG.info("Get ticket count");
        return ticketService.getTotalTicketCount();
    }

    private List<EpamTicket> getEpamTickets(List<CsTicketModel> csTicketModels) {
        List<EpamTicket> tickets = new ArrayList<>();
        (csTicketModels).forEach(csTicketModel -> tickets.add(ticketConverter.convert(csTicketModel)));
        return tickets;
    }

}
