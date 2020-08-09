# kafka-demo

~~~
#Docker image for kafka
docker run -d --rm --name kafka -p 2181:2181 -p 3030:3030 -p 9081-9083:8081-8083 -p 9581-9585:9581-9585 -p 9092:9092 -e ADV_HOST=localhost lensesio/fast-data-dev:latest

