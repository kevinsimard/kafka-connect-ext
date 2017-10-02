package com.kevinsimard.kafka.connect.s3.plain;

import io.confluent.connect.s3.S3SinkConnectorConfig;
import io.confluent.connect.s3.storage.S3OutputStream;
import io.confluent.connect.s3.storage.S3Storage;
import io.confluent.connect.storage.format.Format;
import io.confluent.connect.storage.format.RecordWriter;
import io.confluent.connect.storage.format.RecordWriterProvider;
import io.confluent.connect.storage.format.SchemaFileReader;
import io.confluent.connect.storage.hive.HiveFactory;
import org.apache.kafka.connect.errors.ConnectException;
import org.apache.kafka.connect.sink.SinkRecord;

import java.io.IOException;

public class PlainTextFormat implements Format<S3SinkConnectorConfig, String> {

    private final S3Storage storage;

    public PlainTextFormat(S3Storage storage) {
        this.storage = storage;
    }

    @Override
    public RecordWriterProvider<S3SinkConnectorConfig> getRecordWriterProvider() {
        return new RecordWriterProvider<S3SinkConnectorConfig>() {
            @Override
            public RecordWriter getRecordWriter(final S3SinkConnectorConfig conf, final String fileName) {
                return new RecordWriter() {
                    private final S3OutputStream s3 = storage.create(fileName, true);

                    @Override
                    public void write(SinkRecord record) {
                        try {
                            s3.write(((String) record.value()).getBytes());
                            s3.write("\n".getBytes());
                        } catch (IOException e) {
                            throw new ConnectException(e);
                        }
                    }

                    @Override
                    public void commit() {
                        try {
                            s3.commit();
                        } catch (IOException e) {
                            throw new ConnectException(e);
                        }
                    }

                    @Override
                    public void close() {
                        try {
                            s3.close();
                        } catch (IOException e) {
                            throw new ConnectException(e);
                        }
                    }
                };
            }

            @Override
            public String getExtension() {
                return ".txt";
            }
        };
    }

    @Override
    public SchemaFileReader<S3SinkConnectorConfig, String> getSchemaFileReader() {
        return null;
    }

    @Override
    public HiveFactory getHiveFactory() {
        return null;
    }
}
