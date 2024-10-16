package uk.gov.companieshouse.acspprofile.consumer.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import java.time.LocalDate;
import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.acspprofile.SoleTraderDetails;
import uk.gov.companieshouse.api.delta.AcspSoleTraderDetails;

class SoleTraderDetailsMapperTest {

    private static final String FORENAME = "forename";
    private static final String OTHER_FORENAMES = "middle name";
    private static final String SURNAME = "surname";
    private static final String BRITISH = "british";
    private static final String USUAL_RESIDENTIAL_COUNTRY = "united kingdom";
    private static final String DATE_OF_BIRTH_STRING = "20000827";
    private static final LocalDate DATE_OF_BIRTH = LocalDate.of(2000, 8, 27);

    private final SoleTraderDetailsMapper soleTraderDetailsMapper = new SoleTraderDetailsMapper();

    @Test
    void mapAcspSoleTraderDetails() {
        // given
        AcspSoleTraderDetails acspSoleTraderDetails = new AcspSoleTraderDetails()
                .forename(FORENAME)
                .middleName(OTHER_FORENAMES)
                .surname(SURNAME)
                .nationality(BRITISH)
                .usualCountryOfResidence(USUAL_RESIDENTIAL_COUNTRY)
                .dateOfBirth(DATE_OF_BIRTH_STRING);

        SoleTraderDetails expected = new SoleTraderDetails()
                .forename(FORENAME)
                .otherForenames(OTHER_FORENAMES)
                .surname(SURNAME)
                .nationality(BRITISH)
                .usualResidentialCountry(USUAL_RESIDENTIAL_COUNTRY)
                .dateOfBirth(DATE_OF_BIRTH);

        // when
        SoleTraderDetails actual = soleTraderDetailsMapper.map(acspSoleTraderDetails);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void mapNullAcspSoleTraderDetails() {
        // given

        // when
        SoleTraderDetails actual = soleTraderDetailsMapper.map(null);

        // then
        assertNull(actual);
    }
}