package uk.gov.companieshouse.acspprofile.consumer.kafka;

import com.fasterxml.jackson.annotation.JsonInclude.Include;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.github.tomakehurst.wiremock.http.Request;
import com.github.tomakehurst.wiremock.http.RequestMethod;
import com.github.tomakehurst.wiremock.matching.MatchResult;
import com.github.tomakehurst.wiremock.matching.ValueMatcher;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;

public class PutRequestMatcher implements ValueMatcher<Request> {

    private static final ObjectMapper objectMapper = new ObjectMapper()
            .setSerializationInclusion(Include.NON_EMPTY)
            .registerModule(new JavaTimeModule());

    private final String expectedUrl;
    private final String expectedBody;

    public PutRequestMatcher(String expectedUrl, String expectedBody) {
        this.expectedUrl = expectedUrl;
        this.expectedBody = expectedBody;
    }

    @Override
    public MatchResult match(Request value) {
        return MatchResult.aggregate(
                matchUrl(value.getUrl()),
                matchMethod(value.getMethod()),
                matchBody(value.getBodyAsString()));
    }

    private MatchResult matchUrl(String actualUrl) {
        return MatchResult.of(expectedUrl.equals(actualUrl));
    }

    private MatchResult matchMethod(RequestMethod actualMethod) {
        return MatchResult.of(RequestMethod.PUT.equals(actualMethod));
    }

    private MatchResult matchBody(String actualBody) {
        try {
            InternalAcspApi expected = objectMapper.readValue(expectedBody, InternalAcspApi.class);
            InternalAcspApi actual = objectMapper.readValue(actualBody, InternalAcspApi.class);

            MatchResult result = MatchResult.of(expected.equals(actual));
            if (!result.isExactMatch()) {
                System.out.printf("%nExpected: [%s]%n", expected);
                System.out.printf("%nActual: [%s]", actual);
            }
            return result;
        } catch (JsonProcessingException ex) {
            return MatchResult.of(false);
        }
    }
}

