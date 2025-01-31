package uk.gov.companieshouse.acspprofile.consumer.serdes;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.apache.avro.AvroRuntimeException;
import org.apache.avro.io.DatumWriter;
import org.apache.avro.io.Encoder;
import org.apache.avro.io.EncoderFactory;
import org.apache.avro.reflect.ReflectDatumWriter;
import org.apache.avro.specific.SpecificDatumWriter;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import uk.gov.companieshouse.acspprofile.consumer.exception.NonRetryableException;
import uk.gov.companieshouse.delta.ChsDelta;

class ChsDeltaDeserialiserTest {

    private static final String TOPIC = "topic";

    @Test
    void testShouldSuccessfullyDeserialiseChsDelta() throws IOException {
        // given
        ChsDelta delta = new ChsDelta("{}", 0, "context_id", false);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Encoder encoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
        DatumWriter<ChsDelta> writer = new ReflectDatumWriter<>(ChsDelta.class);
        writer.write(delta, encoder);
        try (ChsDeltaDeserialiser deserialiser = new ChsDeltaDeserialiser()) {

            // when
            ChsDelta actual = deserialiser.deserialize(TOPIC, outputStream.toByteArray());

            // then
            assertThat(actual, is(equalTo(delta)));
        }
    }

    @Test
    void testDeserialiseDataThrowsNonRetryableExceptionIfIOExceptionEncountered() throws IOException {
        // given
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        Encoder encoder = EncoderFactory.get().directBinaryEncoder(outputStream, null);
        DatumWriter<String> writer = new SpecificDatumWriter<>(String.class);
        writer.write("hello", encoder);
        try (ChsDeltaDeserialiser deserialiser = new ChsDeltaDeserialiser()) {

            // when
            Executable actual = () -> deserialiser.deserialize(TOPIC, outputStream.toByteArray());

            // then
            NonRetryableException exception = assertThrows(NonRetryableException.class, actual);
            // Note the '\n' is the length prefix of the invalid data sent to the deserialiser
            assertThat(exception.getMessage(), is(equalTo("Invalid payload: [\nhello]")));
            assertThat(exception.getCause(), is(CoreMatchers.instanceOf(IOException.class)));
        }
    }

    @Test
    void testDeserialiseDataThrowsNonRetryableExceptionIfAvroRuntimeExceptionEncountered() {
        // given
        try (ChsDeltaDeserialiser deserialiser = new ChsDeltaDeserialiser()) {

            // when
            Executable actual = () -> deserialiser.deserialize(TOPIC, "invalid".getBytes(StandardCharsets.UTF_8));

            // then
            NonRetryableException exception = assertThrows(NonRetryableException.class, actual);
            assertThat(exception.getMessage(), is(equalTo("Invalid payload: [invalid]")));
            assertThat(exception.getCause(), is(CoreMatchers.instanceOf(AvroRuntimeException.class)));
        }
    }
}
