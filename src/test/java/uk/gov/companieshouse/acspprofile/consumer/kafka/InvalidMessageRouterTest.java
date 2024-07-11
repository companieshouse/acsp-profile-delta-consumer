package uk.gov.companieshouse.acspprofile.consumer.kafka;

import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.header.internals.RecordHeader;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import uk.gov.companieshouse.delta.ChsDelta;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;
import static org.springframework.kafka.support.KafkaHeaders.*;

@ExtendWith(MockitoExtension.class)
class InvalidMessageRouterTest {

    private InvalidMessageRouter invalidMessageRouter;

    @Mock
    private MessageFlags flags;

    @Mock
    private ChsDelta delta;

    @BeforeEach
    void setup() {
        invalidMessageRouter = new InvalidMessageRouter();
        invalidMessageRouter.configure(Map.of("message-flags", flags, "invalid-topic", "invalid"));
    }

    @Test
    void testOnSendRoutesMessageToInvalidMessageTopicIfNonRetryable() {
        // given
        ProducerRecord<String, Object> message = new ProducerRecord<>("main", 0, "key", "an invalid message",
                List.of(new RecordHeader(ORIGINAL_PARTITION, BigInteger.ZERO.toByteArray()),
                        new RecordHeader(ORIGINAL_OFFSET, BigInteger.ONE.toByteArray()),
                        new RecordHeader(EXCEPTION_MESSAGE, "invalid".getBytes())));

        // when
        ProducerRecord<String, Object> actual = invalidMessageRouter.onSend(message);

        // then
        verify(flags, times(0)).destroy();
        assertThat(actual).isEqualTo(new ProducerRecord<>("invalid", "key", "an invalid message"));
    }

    @Test
    void testOnSendRoutesMessageToTargetTopicIfRetryable() {
        // given
        ProducerRecord<String, Object> message = new ProducerRecord<>("main", "key", delta);
        when(flags.isRetryable()).thenReturn(true);

        // when
        ProducerRecord<String, Object> actual = invalidMessageRouter.onSend(message);

        // then
        assertThat(actual).isSameAs((message));
        verify(flags, times(1)).destroy();
    }
}
