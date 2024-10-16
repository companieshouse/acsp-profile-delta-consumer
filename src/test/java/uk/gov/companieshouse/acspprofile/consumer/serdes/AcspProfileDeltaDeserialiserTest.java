package uk.gov.companieshouse.acspprofile.consumer.serdes;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.consumer.exception.NonRetryableException;
import uk.gov.companieshouse.api.delta.AcspProfileDelta;

@ExtendWith(MockitoExtension.class)
class AcspProfileDeltaDeserialiserTest {

    public static final String ACSP_PROFILE_DELTA = "acsp profile delta json string";

    @InjectMocks
    private AcspProfileDeltaDeserialiser deserialiser;
    @Mock
    private ObjectMapper objectMapper;

    @Mock
    private AcspProfileDelta expectedDelta;

    @Test
    void shouldDeserialiseAcspProfileDelta() throws JsonProcessingException {
        // given
        when(objectMapper.readValue(anyString(), eq(AcspProfileDelta.class))).thenReturn(expectedDelta);

        // when
        AcspProfileDelta actual = deserialiser.deserialiseAcspProfileDelta(ACSP_PROFILE_DELTA);

        // then
        assertEquals(expectedDelta, actual);
        verify(objectMapper).readValue(ACSP_PROFILE_DELTA, AcspProfileDelta.class);
    }

    @Test
    void shouldThrowNonRetryableExceptionWhenJsonProcessingExceptionThrown() throws JsonProcessingException {
        // given
        when(objectMapper.readValue(anyString(), eq(AcspProfileDelta.class))).thenThrow(
                JsonProcessingException.class);

        // when
        Executable executable = () -> deserialiser.deserialiseAcspProfileDelta(ACSP_PROFILE_DELTA);

        // then
        NonRetryableException actual = assertThrows(NonRetryableException.class, executable);
        assertEquals("Unable to deserialise delta", actual.getMessage());
        verify(objectMapper).readValue(ACSP_PROFILE_DELTA, AcspProfileDelta.class);
    }
}