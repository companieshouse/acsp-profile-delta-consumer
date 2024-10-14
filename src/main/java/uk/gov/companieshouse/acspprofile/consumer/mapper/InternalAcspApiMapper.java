package uk.gov.companieshouse.acspprofile.consumer.mapper;

import static uk.gov.companieshouse.acspprofile.consumer.mapper.DateUtils.stringToLocalDate;

import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.acspprofile.AcspFullProfile;
import uk.gov.companieshouse.api.acspprofile.BusinessSector;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;
import uk.gov.companieshouse.api.acspprofile.InternalData;
import uk.gov.companieshouse.api.acspprofile.Links;
import uk.gov.companieshouse.api.acspprofile.Status;
import uk.gov.companieshouse.api.acspprofile.Type;
import uk.gov.companieshouse.api.delta.AcspProfileDelta;

@Component
public class InternalAcspApiMapper {

    private static final String DELTA_TYPE = "acsp_delta";
    private static final String SELF_LINK = "/authorised-corporate-service-providers/%s";

    private final AddressMapper addressMapper;
    private final SoleTraderDetailsMapper soleTraderDetailsMapper;
    private final AmlDetailsMapper amlDetailsMapper;
    private final EtagGenerator etagGenerator;

    public InternalAcspApiMapper(AddressMapper addressMapper, SoleTraderDetailsMapper soleTraderDetailsMapper,
            AmlDetailsMapper amlDetailsMapper, EtagGenerator etagGenerator) {
        this.addressMapper = addressMapper;
        this.soleTraderDetailsMapper = soleTraderDetailsMapper;
        this.amlDetailsMapper = amlDetailsMapper;
        this.etagGenerator = etagGenerator;
    }

    public InternalAcspApi map(AcspProfileDelta delta, String contextId) {
        String acspNumber = delta.getAcspNumber();
        AcspFullProfile fullProfile = new AcspFullProfile()
                .number(acspNumber)
                .name(delta.getAcspName())
                .type(Type.fromValue(delta.getType()))
                .notifiedFrom(stringToLocalDate(delta.getNotifiedFrom()))
                .deauthorisedFrom(stringToLocalDate(delta.getDeauthorisedFrom()))
                .businessSector(delta.getBusinessSector() != null ?
                        BusinessSector.fromValue(delta.getBusinessSector()) : null)
                .status(Status.fromValue(delta.getStatus()))
                .registeredOfficeAddress(addressMapper.map(delta.getRegisteredOfficeAddress()))
                .serviceAddress(addressMapper.map(delta.getServiceAddress()))
                .soleTraderDetails(soleTraderDetailsMapper.map(delta.getSoleTraderDetails()))
                .amlDetails(amlDetailsMapper.map(delta.getAmlDetails()))
                .email(delta.getEmail())
                .etag(etagGenerator.generateEtag())
                .links(new Links()
                        .self(SELF_LINK.formatted(acspNumber)));
        InternalData internalData = new InternalData()
                .updatedType(DELTA_TYPE)
                .updatedBy(contextId)
                .deltaAt(delta.getDeltaAt());
        return new InternalAcspApi()
                .acspFullProfile(fullProfile)
                .internalData(internalData);
    }
}
