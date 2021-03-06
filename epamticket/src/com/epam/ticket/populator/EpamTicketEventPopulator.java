package com.epam.ticket.populator;

import com.epam.dto.EpamTicketChangeEventEntry;
import com.epam.dto.EpamTicketEvent;
import com.epam.ticket.converter.EpamTicketChangeEventEntryConverter;
import com.epam.ticket.converter.EpamTicketEmailConverter;
import de.hybris.platform.converters.Populator;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import de.hybris.platform.ticket.events.model.CsTicketEventModel;

import java.util.stream.Collectors;

import static com.google.common.base.Preconditions.checkNotNull;

public class EpamTicketEventPopulator implements Populator<CsTicketEventModel, EpamTicketEvent> {

    private EpamTicketEmailConverter ticketEmailConverter;
    private EpamTicketChangeEventEntryConverter ticketChangeEventEntryConverter;

    public void setTicketEmailConverter(EpamTicketEmailConverter ticketEmailConverter) {
        this.ticketEmailConverter = ticketEmailConverter;
    }

    public void setTicketChangeEventEntryConverter(EpamTicketChangeEventEntryConverter ticketChangeEventEntryConverter) {
        this.ticketChangeEventEntryConverter = ticketChangeEventEntryConverter;
    }

    @Override
    public void populate(CsTicketEventModel source, EpamTicketEvent target) throws ConversionException {
        checkNotNull(source, "Source model should not be null");
        target.setStartDateTime(source.getStartDateTime());
        target.setEndDateTime(source.getEndDateTime());
        target.setText(source.getText());
        target.setEmails(source.getEmails().parallelStream()
                .map(ticketEmailConverter::convert)
                .collect(Collectors.toList()));
        target.setTicketChangeEventEntries(source.getEntries().stream()
                .map(ticketChangeEventEntryConverter::convert)
                .collect(Collectors.<EpamTicketChangeEventEntry>toSet()));
    }
}
