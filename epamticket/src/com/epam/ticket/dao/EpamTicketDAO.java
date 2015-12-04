package com.epam.ticket.dao;

import java.io.Serializable;
import org.apache.log4j.Logger;

import com.epam.ticket.dao.counters.CategoryCounterStrategy;
import com.epam.ticket.facades.EpamTicketSearchCriteria;

import de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException;
import de.hybris.platform.servicelayer.search.SearchResult;
import de.hybris.platform.ticket.dao.impl.DefaultTicketDao;
import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;
import de.hybris.platform.ticket.model.CsTicketModel;

import java.util.*;

import static com.google.common.base.Strings.isNullOrEmpty;

public class EpamTicketDAO extends DefaultTicketDao {

    public static final Logger LOG = Logger.getLogger(EpamTicketDAO.class);
    public static final String QUERY_STRING = "SELECT {t:pk} FROM {CsTicket AS t} ";
    private StringBuffer query;
    private CategoryCounterStrategy enumCategoryCounterStrategy;

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

        String field = criteria.getSortName(); //todo validate field
        if (!isNullOrEmpty(field)) {
            EpamCsSort sort = sorts.get(criteria.getSortName());
            if(sort == null)
                throw new IllegalArgumentException("Sort " + criteria.getSortName() + " not found");
            query.append("ORDER BY {t.");
            query.append(sort.getFlexField()).append("} "); // danger, may cause FlexSearch manipulation
            query.append(criteria.getSortReverse()
                    ? "DESC" : "ASC");

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
    
    public TicketCountsResult getTicketCounts() {
        TicketCountsResult result = new TicketCountsResult();

        result.addFilerCategoryCounters("priority", enumCategoryCounterStrategy.countCategory("priority"));
        result.addFilerCategoryCounters("state", enumCategoryCounterStrategy.countCategory("state"));
        
        LOG.info("Ticket counts:" + result);
        return result;
    }

    private String getJoiningString() {
        return query.length() == QUERY_STRING.length() ? " WHERE " : " AND ";

    }
    
    public class TicketCountsResult implements Serializable {
        private static final long serialVersionUID = 1L;
        private final Map<String, Map<String, Integer>> filterCategories = new HashMap<>();
        
        public void addFilerCategoryCounters(final String filterCategory, final Map<String, Integer> categoryStates) {
            filterCategories.put(filterCategory, categoryStates);
        }
        
        public Map<String, Map<String, Integer>> getFilterCategories() {
            return filterCategories;
        }

    }

    protected Map<String, EpamCsSort> sorts = new HashMap<>();

    public Collection<EpamCsSort> getAvailableSorts() {
        return sorts.values();
    }

    public void setAvailableSorts(Collection<EpamCsSort> sorts) {
        Map<String, EpamCsSort> res = new HashMap<>();
        for (EpamCsSort sort : sorts) {
            res.put(sort.getName(), sort);
        }
        this.sorts = res;
    }

    public void setEnumCategoryCounterStrategy(CategoryCounterStrategy enumCategoryCounterStrategy) {
        this.enumCategoryCounterStrategy = enumCategoryCounterStrategy;
    }
}
