package uk.gov.companieshouse.acspprofile.consumer.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.consumer.mapper.InternalAcspApiMapper;
import uk.gov.companieshouse.acspprofile.consumer.serdes.AcspProfileDeltaDeserialiser;
import uk.gov.companieshouse.api.acspprofile.InternalAcspApi;
import uk.gov.companieshouse.api.delta.AcspProfileDelta;
import uk.gov.companieshouse.delta.ChsDelta;

@ExtendWith(MockitoExtension.class)
class UpsertDeltaServiceTest {

    private static final String DELTA_DATA = "acsp profile delta data";
    private static final String CONTEXT_ID = "context_id";

    @InjectMocks
    private UpsertDeltaService upsertDeltaService;
    @Mock
    private AcspProfileDeltaDeserialiser deltaDeserialiser;
    @Mock
    private InternalAcspApiMapper mapper;

    @Mock
    private AcspProfileDelta acspProfileDelta;
    @Mock
    private InternalAcspApi internalAcspApi;

    @Test
    void shouldProcessDelta() {
        // given
        ChsDelta delta = new ChsDelta(DELTA_DATA, 0, CONTEXT_ID, false);

        when(deltaDeserialiser.deserialiseAcspProfileDelta(any())).thenReturn(acspProfileDelta);
        when(mapper.map(any(), any())).thenReturn(internalAcspApi);

        // when
        upsertDeltaService.process(delta);

        // then
        verify(deltaDeserialiser).deserialiseAcspProfileDelta(DELTA_DATA);
        verify(mapper).map(acspProfileDelta, CONTEXT_ID);
        verifyNoInteractions(internalAcspApi);
    }
}