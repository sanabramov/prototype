package com.epam.ticket.services;

import com.epam.ticket.facades.EpamTicketSearchCriteria;
import de.hybris.platform.ticket.model.CsTicketModel;

import java.util.List;

/**
 * @author Dmitry Adonin
 * @since 27-Nov-15
 */
public interface EpamTicketService {

    List<CsTicketModel> getTicketsByCriteria(EpamTicketSearchCriteria searchCriteria);

    CsTicketModel getTicketById(String ticketId);
}