package uk.gov.companieshouse.acspprofile.consumer.mapper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.Address;
import uk.gov.companieshouse.api.acspprofile.AmlDetailsItem;
import uk.gov.companieshouse.api.acspprofile.BusinessSector;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;
import uk.gov.companieshouse.api.acspprofile.InternalData;
import uk.gov.companieshouse.api.acspprofile.Links;
import uk.gov.companieshouse.api.acspprofile.SoleTraderDetails;
import uk.gov.companieshouse.api.acspprofile.Status;
import uk.gov.companieshouse.api.acspprofile.Type;
import uk.gov.companieshouse.api.delta.AcspAddress;
import uk.gov.companieshouse.api.delta.AcspAmlDetails;
import uk.gov.companieshouse.api.delta.AcspProfileDelta;
import uk.gov.companieshouse.api.delta.AcspSoleTraderDetails;

@ExtendWith(MockitoExtension.class)
class InternalAcspApiMapperTest {

    private static final String ACSP_NUMBER = "AP123456";
    private static final String NAME = "acsp name";
    private static final String NOTIFIED_FROM_STRING = "20240823";
    private static final String DEAUTHORISED_FROM_STRING = "20240825";
    private static final LocalDate NOTIFIED_FROM = LocalDate.of(2024, 8, 23);
    private static final LocalDate DEAUTHORISED_FROM = LocalDate.of(2024, 8, 25);
    private static final String STATUS = "active";
    private static final String TYPE = "corporate-body";
    private static final String EMAIL = "email";
    private static final String BUSINESS_SECTOR = "financial-institutions";
    private static final String DELTA_AT = "20241010175532456123";
    private static final String DELTA_TYPE = "acsp_delta";
    private static final String CONTEXT_ID = "context_id";
    private static final String SELF_LINK = "/authorised-corporate-service-providers/%s".formatted(ACSP_NUMBER);
    private static final String ETAG = "etag";

    @InjectMocks
    private InternalAcspApiMapper mapper;
    @Mock
    private AddressMapper addressMapper;
    @Mock
    private SoleTraderDetailsMapper soleTraderDetailsMapper;
    @Mock
    private AmlDetailsMapper amlDetailsMapper;
    @Mock
    private EtagGenerator etagGenerator;

    @Mock
    private AcspAddress acspAddress;
    @Mock
    private Address expectedAddress;
    @Mock
    private AcspSoleTraderDetails acspSoleTraderDetails;
    @Mock
    private SoleTraderDetails expectedSoleTraderDetails;
    @Mock
    private AcspAmlDetails acspAmlDetails;
    @Mock
    private AmlDetailsItem expectedAmlDetailsItem;

    @Test
    void shouldMapAcspDeltaToApiRequest() {
        // given
        AcspProfileDelta delta = new AcspProfileDelta()
                .acspNumber(ACSP_NUMBER)
                .acspName(NAME)
                .status(STATUS)
                .type(TYPE)
                .notifiedFrom(NOTIFIED_FROM_STRING)
                .deauthorisedFrom(DEAUTHORISED_FROM_STRING)
                .businessSector(BUSINESS_SECTOR)
                .registeredOfficeAddress(acspAddress)
                .serviceAddress(acspAddress)
                .soleTraderDetails(acspSoleTraderDetails)
                .amlDetails(List.of(acspAmlDetails))
                .email(EMAIL)
                .deltaAt(DELTA_AT);

        AcspFullProfile fullProfile = new AcspFullProfile()
                .number(ACSP_NUMBER)
                .name(NAME)
                .status(Status.ACTIVE)
                .type(Type.CORPORATE_BODY)
                .businessSector(BusinessSector.FINANCIAL_INSTITUTIONS)
                .notifiedFrom(NOTIFIED_FROM)
                .deauthorisedFrom(DEAUTHORISED_FROM)
                .registeredOfficeAddress(expectedAddress)
                .serviceAddress(expectedAddress)
                .soleTraderDetails(expectedSoleTraderDetails)
                .amlDetails(List.of(expectedAmlDetailsItem))
                .email(EMAIL)
                .etag(ETAG)
                .links(new Links()
                        .self(SELF_LINK));

        InternalAcspApi expected = new InternalAcspApi()
                .acspFullProfile(fullProfile)
                .internalData(new InternalData()
                        .updatedType(DELTA_TYPE)
                        .updatedBy(CONTEXT_ID)
                        .deltaAt(DELTA_AT));

        when(addressMapper.map(any())).thenReturn(expectedAddress);
        when(soleTraderDetailsMapper.map(any())).thenReturn(expectedSoleTraderDetails);
        when(amlDetailsMapper.map(any())).thenReturn(List.of(expectedAmlDetailsItem));
        when(etagGenerator.generateEtag()).thenReturn(ETAG);

        // when
        InternalAcspApi actual = mapper.map(delta, CONTEXT_ID);

        // then
        assertEquals(expected, actual);
        verify(addressMapper, times(2)).map(acspAddress);
        verify(soleTraderDetailsMapper).map(acspSoleTraderDetails);
        verify(amlDetailsMapper).map(List.of(acspAmlDetails));
        verify(etagGenerator).generateEtag();
    }

    @Test
    void shouldMapAcspDeltaWithOnlyRequiredFieldsToApiRequest() {
        // given
        AcspProfileDelta delta = new AcspProfileDelta()
                .acspNumber(ACSP_NUMBER)
                .acspName(NAME)
                .status(STATUS)
                .type(TYPE)
                .notifiedFrom(NOTIFIED_FROM_STRING)
                .registeredOfficeAddress(acspAddress)
                .email(EMAIL)
                .deltaAt(DELTA_AT);

        AcspFullProfile fullProfile = new AcspFullProfile()
                .number(ACSP_NUMBER)
                .name(NAME)
                .status(Status.ACTIVE)
                .type(Type.CORPORATE_BODY)
                .notifiedFrom(NOTIFIED_FROM)
                .registeredOfficeAddress(expectedAddress)
                .email(EMAIL)
                .etag(ETAG)
                .links(new Links()
                        .self(SELF_LINK));

        InternalAcspApi expected = new InternalAcspApi()
                .acspFullProfile(fullProfile)
                .internalData(new InternalData()
                        .updatedType(DELTA_TYPE)
                        .updatedBy(CONTEXT_ID)
                        .deltaAt(DELTA_AT));

        when(addressMapper.map(any())).thenReturn(expectedAddress, (Address) null);
        when(soleTraderDetailsMapper.map(any())).thenReturn(null);
        when(amlDetailsMapper.map(any())).thenReturn(null);
        when(etagGenerator.generateEtag()).thenReturn(ETAG);

        // when
        InternalAcspApi actual = mapper.map(delta, CONTEXT_ID);

        // then
        assertEquals(expected, actual);
        verify(addressMapper).map(acspAddress);
        verify(addressMapper).map(null);
        verify(soleTraderDetailsMapper).map(null);
        verify(amlDetailsMapper).map(null);
        verify(etagGenerator).generateEtag();
    }
}