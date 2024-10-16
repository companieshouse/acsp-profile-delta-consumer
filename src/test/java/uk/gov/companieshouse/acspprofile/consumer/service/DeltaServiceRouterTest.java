package uk.gov.companieshouse.acspprofile.consumer.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.function.Executable;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.acspprofile.consumer.exception.NonRetryableException;
import uk.gov.companieshouse.delta.ChsDelta;

@ExtendWith(MockitoExtension.class)
class DeltaServiceRouterTest {

    @InjectMocks
    private DeltaServiceRouter router;
    @Mock
    private UpsertDeltaService upsertDeltaService;

    @Test
    void shouldRouteDeltaToUpsertService() {
        // given
        ChsDelta delta = new ChsDelta();

        // when
        router.route(delta);

        // then
        verify(upsertDeltaService).process(delta);
    }

    @Test
    void shouldThrowNonRetryableWhenIsDelete() {
        // given
        ChsDelta delta = new ChsDelta();
        delta.setIsDelete(true);

        // when
        Executable actual = () -> router.route(delta);

        // then
        NonRetryableException exception = assertThrows(NonRetryableException.class, actual);
        assertEquals("Delete operation is not currently supported", exception.getMessage());
        verifyNoInteractions(upsertDeltaService);
    }
}