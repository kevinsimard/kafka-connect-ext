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
import java.io.OutputStream;
import java.util.zip.DeflaterOutputStream;
import java.util.zip.GZIPOutputStream;

public class GzipTextFormat implements Format<S3SinkConnectorConfig, String> {

    private final S3Storage storage;

    public GzipTextFormat(S3Storage storage) {
        this.storage = storage;
    }

    @Override
    public RecordWriterProvider<S3SinkConnectorConfig> getRecordWriterProvider() {
        return new RecordWriterProvider<S3SinkConnectorConfig>() {
            @Override
            public RecordWriter getRecordWriter(S3SinkConnectorConfig conf, String fileName) {
                try {
                    return new RecordWriter() {
                        private final S3OutputStream s3 = storage.create(fileName, true);
                        private final OutputStream out = new GZIPOutputStream(s3);

                        @Override
                        public void write(SinkRecord record) {
                            try {
                                out.write(((String) record.value()).getBytes());
                                out.write("\n".getBytes());
                            } catch (IOException e) {
                                throw new ConnectException(e);
                            }
                        }

                        @Override
                        public void commit() {
                            try {
                                out.flush();

                                // Finishes writing compressed data to the output
                                // stream without closing the underlying stream.
                                ((DeflaterOutputStream) out).finish();

                                s3.commit();
                                out.close();
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
                } catch (IOException e) {
                    throw new ConnectException(e);
                }
            }

            @Override
            public String getExtension() {
                return ".txt.gz";
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
