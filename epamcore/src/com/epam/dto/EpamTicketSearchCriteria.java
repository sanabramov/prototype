package com.epam.dto;

import de.hybris.platform.ticket.enums.CsTicketCategory;
import de.hybris.platform.ticket.enums.CsTicketPriority;
import de.hybris.platform.ticket.enums.CsTicketState;

import java.io.Serializable;
import java.util.List;

public class EpamTicketSearchCriteria implements Serializable {

    private List<CsTicketPriority> priorities;
    private List<CsTicketState> states;
    private List<CsTicketCategory> categories;
    private String agentId;
    private String sortName;
    private Boolean sortReverse = Boolean.FALSE;

    public void setPriorities(final List<CsTicketPriority> priorities) {
        this.priorities = priorities;
    }

    public List<CsTicketPriority> getPriorities() {
        return priorities;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(final String agentId) {
        this.agentId = agentId;
    }

    public List<CsTicketState> getStates() {
        return states;
    }

    public void setStates(final List<CsTicketState> states) {
        this.states = states;
    }

    public List<CsTicketCategory> getCategories() {
        return categories;
    }

    public void setCategories(final List<CsTicketCategory> categories) {
        this.categories = categories;
    }

    public String getSortName() {
        return sortName;
    }

    public void setSortName(final String sortName) {
        this.sortName = sortName;
    }

    public Boolean getSortReverse() {
        return sortReverse;
    }

    public void setSortReverse(final Boolean sortReverse) {
        this.sortReverse = sortReverse;
    }

    @Override
    public String toString() {
        return "EpamTicketSearchCriteria{" + "priorities=" + priorities + "states=" + states + ", agentId='" + agentId + '\'' + '}';
    }

}