package uk.gov.companieshouse.acspprofile.consumer.mapper;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Component;
import uk.gov.companieshouse.api.acspprofile.AmlDetailsItem;
import uk.gov.companieshouse.api.acspprofile.SupervisoryBody;
import uk.gov.companieshouse.api.delta.AcspAmlDetails;

@Component
public class AmlDetailsMapper {

    public List<AmlDetailsItem> map(List<AcspAmlDetails> acspAmlDetails) {
        return Optional.ofNullable(acspAmlDetails)
                .map(amlDetails -> amlDetails.stream()
                        .map(amlDetail -> new AmlDetailsItem()
                                .supervisoryBody(amlDetail.getSupervisoryBody() != null ?
                                        SupervisoryBody.fromValue(amlDetail.getSupervisoryBody()) : null)
                                .membershipDetails(amlDetail.getMembershipDetails()))
                        .toList())
                .filter(details -> !details.isEmpty())
                .orElse(null);
    }
}
