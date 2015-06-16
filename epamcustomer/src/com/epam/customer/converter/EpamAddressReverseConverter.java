package com.epam.customer.converter;

import com.epam.customer.converter.populator.EpamAddressReversePopulator;
import com.epam.customer.data.EpamAddressData;
import de.hybris.platform.converters.impl.AbstractPopulatingConverter;
import de.hybris.platform.core.model.user.AddressModel;
import de.hybris.platform.servicelayer.dto.converter.ConversionException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Component;

/**
 * @author Roman_Kovalenkol
 */
@Component
@Qualifier("reverseConverter")
public class EpamAddressReverseConverter extends AbstractPopulatingConverter<EpamAddressData, AddressModel> {

    private EpamAddressReversePopulator customerAddressReversePopulator;

    @Autowired // TODO Switch to XML-based configuration
    public EpamAddressReverseConverter(EpamAddressReversePopulator customerAddressReversePopulator) {
        this.customerAddressReversePopulator = customerAddressReversePopulator;
    }

    @Override
    protected AddressModel createTarget() {
        return new AddressModel();
    }

    @Override
    public AddressModel convert(EpamAddressData addressData) throws ConversionException {
        AddressModel addressModel = createTarget();
        customerAddressReversePopulator.populate(addressData, addressModel);
        return addressModel;
    }
}