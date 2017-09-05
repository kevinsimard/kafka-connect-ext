# Kafka Connect HDFS Extension

## Code Structure

    ├── bin
    │   └── kafka-connect-hdfs-ext-1.0.0.jar
    ├── src
    │   └── main
    │       └── java
    │           └── com
    │               └── kevinsimard
    │                   └── kafka
    │                       └── connect
    │                           └── hdfs
    │                               └── ext
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
$ cp target/kafka-connect-hdfs-ext-1.0.0.jar <connect_path>/share/java/kafka-connect-hdfs/
$ ln -s target/kafka-connect-hdfs-ext-1.0.0.jar <connect_path>/share/java/kafka-connect-hdfs/
```

## Usage

Set format class in your HDFS sink properties file to one of the following:

```
format.class=com.kevinsimard.kafka.connect.hdfs.ext.GzipTextFormat
format.class=com.kevinsimard.kafka.connect.hdfs.ext.PlainTextFormat
```

> Note: Use instructions from https://github.com/confluentinc/kafka-connect-hdfs for other required configurations.
