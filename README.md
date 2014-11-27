```
UNDER CONSTRUCTION - DO NOT USE
```

### Setup

Install AKKA-Kafka for Scala 2.11:

    git clone git@github.com:sclasen/akka-kafka.git
    cd akka-kafka
    git checkout kafka-082-beta
    sbt publish-local

Starting zookeeper:

    zookeeper-server-start.sh config/zookeeper.properties

Starting Kafka:

    kafka-server-start.sh config/kafka-server.properties
