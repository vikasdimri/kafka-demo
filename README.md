# kafka-demo

```
#Docker image for kafka
docker run -d --rm --name kafka -p 2181:2181 -p 3030:3030 -p 9081-9083:8081-8083 -p 9581-9585:9581-9585 -p 9092:9092 -e ADV_HOST=localhost lensesio/fast-data-dev:latest

# Loging into kafka container
docker exec -it kafka /bin/bash

# Create topic
kafka-topics --create --bootstrap-server "localhost:9092" --replication-factor 1 --partitions 3 --topic address

# Describe topic
kafka-topics --describe --bootstrap-server "localhost:9092" --topic address

# List of topic
kafka-topics --list --bootstrap-server "localhost:9092"
```
