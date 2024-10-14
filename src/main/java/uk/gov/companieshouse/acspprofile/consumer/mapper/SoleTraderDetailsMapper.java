package uk.gov.companieshouse.acspprofile.consumer.mapper;

import static uk.gov.companieshouse.acspprofile.consumer.mapper.DateUtils.stringToLocalDate;

import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.acspprofile.SoleTraderDetails;
import uk.gov.companieshouse.api.delta.AcspSoleTraderDetails;

@Component
public class SoleTraderDetailsMapper {

    public SoleTraderDetails map(AcspSoleTraderDetails acspSoleTraderDetails) {
        return Optional.ofNullable(acspSoleTraderDetails)
                .map(details -> new SoleTraderDetails()
                        .forename(details.getForename())
                        .otherForenames(details.getMiddleName())
                        .surname(details.getSurname())
                        .nationality(details.getNationality())
                        .usualResidentialCountry(details.getUsualCountryOfResidence())
                        .dateOfBirth(stringToLocalDate(details.getDateOfBirth())))
                .orElse(null);
    }
}
