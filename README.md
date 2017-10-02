# Kafka Connect Extension

## Code Structure

    ├── src
    │   └── main
    │       └── java
    │           └── com
    │               └── kevinsimard
    │                   └── kafka
    │                       └── connect
    │                           ├── hdfs
    │                           │   └── plain
    │                           │       ├── GzipTextFormat.java
    │                           │       └── PlainTextFormat.java
    │                           └── s3
    │                               └── plain
    │                                   ├── GzipTextFormat.java
    │                                   └── PlainTextFormat.java
    ├── .editorconfig
    ├── .gitattributes
    ├── .gitignore
    ├── README.md
    └── pom.xml

## Install

Use `$ mvn package` to compile JAR file and either copy it or create a symlink to Kafka Connect shared directory.

```bash
$ cp target/kafka-connect-ext-1.1.0.jar <connect_path>/share/java/kafka/
$ ln -s target/kafka-connect-ext-1.1.0.jar <connect_path>/share/java/kafka/
```

## Usage

### HDFS

Set format class in your HDFS sink properties file to one of the following:

```
format.class=com.kevinsimard.kafka.connect.hdfs.plain.GzipTextFormat
format.class=com.kevinsimard.kafka.connect.hdfs.plain.PlainTextFormat
```

> Note: Use instructions from https://github.com/confluentinc/kafka-connect-hdfs for other required configurations.

### S3

Set format class in your S3 sink properties file to one of the following:

```
format.class=com.kevinsimard.kafka.connect.s3.plain.GzipTextFormat
format.class=com.kevinsimard.kafka.connect.s3.plain.PlainTextFormat
```

> Note: Use instructions from https://github.com/confluentinc/kafka-connect-storage-cloud for other required configurations.
