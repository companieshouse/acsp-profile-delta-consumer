package uk.gov.companieshouse.acspprofile.consumer.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

import org.junit.jupiter.api.Test;
import uk.gov.companieshouse.api.acspprofile.Address;
import uk.gov.companieshouse.api.delta.AcspAddress;

class AddressMapperTest {

    private static final String CARE_OF = "Jane Smith";
    private static final String ADDRESS_LINE_1 = "456 Another Street";
    private static final String ADDRESS_LINE_2 = "Floor 2";
    private static final String COUNTRY = "United Kingdom";
    private static final String LOCALITY = "Manchester";
    private static final String PO_BOX = "PO Box 123";
    private static final String POSTAL_CODE = "M1 2AB";
    private static final String PREMISES = "Another Building";
    private static final String REGION = "Greater Manchester";

    private final AddressMapper addressMapper = new AddressMapper();

    @Test
    void mapAcspAddress() {
        // given
        AcspAddress acspAddress = new AcspAddress()
                .careOf(CARE_OF)
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .country(COUNTRY)
                .locality(LOCALITY)
                .poBox(PO_BOX)
                .postalCode(POSTAL_CODE)
                .premises(PREMISES)
                .region(REGION);

        Address expected = new Address()
                .careOf(CARE_OF)
                .addressLine1(ADDRESS_LINE_1)
                .addressLine2(ADDRESS_LINE_2)
                .country(COUNTRY)
                .locality(LOCALITY)
                .poBox(PO_BOX)
                .postalCode(POSTAL_CODE)
                .premises(PREMISES)
                .region(REGION);

        // when
        Address actual = addressMapper.map(acspAddress);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void mapBareAcspAddress() {
        // given
        AcspAddress acspAddress = new AcspAddress();

        Address expected = new Address();

        // when
        Address actual = addressMapper.map(acspAddress);

        // then
        assertEquals(expected, actual);
    }

    @Test
    void mapNullAcspAddress() {
        // given

        // when
        Address actual = addressMapper.map(null);

        // then
        assertNull(actual);
    }
}