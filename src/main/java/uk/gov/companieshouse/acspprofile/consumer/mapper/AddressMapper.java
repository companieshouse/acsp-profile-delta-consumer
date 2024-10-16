package uk.gov.companieshouse.acspprofile.consumer.mapper;

import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.acspprofile.Address;
import uk.gov.companieshouse.api.acspprofile.Country;
import uk.gov.companieshouse.api.delta.AcspAddress;

@Component
public class AddressMapper {

    public Address map(AcspAddress acspAddress) {
        return Optional.ofNullable(acspAddress)
                .map(address -> new Address()
                        .addressLine1(address.getAddressLine1())
                        .addressLine2(address.getAddressLine2())
                        .careOf(address.getCareOf())
                        .country(address.getCountry() != null ? Country.fromValue(address.getCountry()) : null)
                        .poBox(address.getPoBox())
                        .locality(address.getLocality())
                        .postalCode(address.getPostalCode())
                        .premises(address.getPremises())
                        .region(address.getRegion()))
                .orElse(null);
    }
}
