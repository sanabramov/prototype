package com.epam.ticket.dao;

import com.epam.ticket.facades.EpamTicketSearchCriteria;
import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.ticket.dao.impl.DefaultTicketDao;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.model.CsTicketModel;
import org.apache.log4j.Logger;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import static com.google.common.base.Strings.isNullOrEmpty;

public class EpamTicketDAO extends DefaultTicketDao {

    public static final Logger LOG = Logger.getLogger(EpamTicketDAO.class);
    public static final String QUERY_STRING = "SELECT {t:pk} FROM {CsTicket AS t} ";
    private StringBuffer query;

    public void addTicket(CsTicketModel ticketModel) {
        getModelService().save(ticketModel);
    }

    public CsTicketModel getTicketById(String ticketId) {
        List<CsTicketModel> csTicketModels = this.findTicketsById(ticketId);
        if (csTicketModels.size() > 1) {
            throw new AmbiguousIdentifierException("CsTicket with ticketId'" + ticketId + "' is not unique, " + csTicketModels.size() + " results!");
        }
        return csTicketModels.size() == 1 ? csTicketModels.get(0) : null;

    }

    public List<CsTicketModel> findTicketsByCriteria(EpamTicketSearchCriteria criteria) {
        query = new StringBuffer(QUERY_STRING);
        Map<String, Object> paramMap = new TreeMap<>();

        List<CsTicketPriority> priorities = criteria.getPriorities();
        if (priorities != null && priorities.size() != 0) {
            query.append(getJoiningString());
            query.append("{priority} IN (?priority)");
            paramMap.put("priority", priorities);
        }

        List<CsTicketState> states = criteria.getStates();
        if (states != null && states.size() != 0) {
            query.append(getJoiningString());
            query.append("{state} IN (?state)");
            paramMap.put("state", states);
        }

        List<CsTicketCategory> categories = criteria.getCategories();
        if (categories != null && categories.size() != 0) {
            query.append(getJoiningString());
            query.append("{category} IN (?category)");
            paramMap.put("category", categories);
        }

        String agentId = criteria.getAgentId();
        if (!isNullOrEmpty(agentId)) {
            query.append(getJoiningString());
            query.append("{assignedAgent} = ?agent");
            paramMap.put("agent", agentId);
        }

        LOG.info("Running query: " + query + " with params: " + paramMap);
        SearchResult<CsTicketModel> resultTickets = getFlexibleSearchService()
                .search(query.toString(), paramMap);

        return resultTickets.getResult();
    }

    public Integer getTotalTicketCount() {
        SearchResult result = getFlexibleSearchService().search("SELECT {pk} FROM {CsTicket}");
        int totalCount = result.getTotalCount();
        LOG.info("Ticket count:" + totalCount);
        return totalCount;
    }

    private String getJoiningString() {
        return query.length() == QUERY_STRING.length() ? " WHERE " : " AND ";

    }

}
