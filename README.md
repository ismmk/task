# POC for Log Gathering service. Consists of : 

## Producer 
Simple Http client that pushes incoming POST requests to kafka
## Processor 
Spark Stream Proc Job that reads data from kafka, parse it and put to Cassandra storage
## Provider 
Simple Web Application that provide endpoints for retriving log stats data.

----
There is also a simple golang app that generates random log entries and POST them to Producer.


There is only 1 endpoint in Provider currently - /api/access/countct?type=_type_&client=_client_ that returns 
number of log entries for specified params. New endpoitns will be provided on demands.

There are plans to extend functionality on Processor layer (MLib, new views in storage, etc).

Project contains docker-compose config file for test purposes.


## Env Variables
### Producer
#### KAFKA_PEERS - The Kafka brokers to connect to
### Processor
#### CASSANDRA_HOST - Cassandra host
#### CASSANDRA_PORT - Cassandra port
#### CASSANDRA_KEY_STORE - Cassandra key store name
#### ZOOKEEPER_HOSTS - Zookeeper hosts wher e
#### KAFKA_GROUP_ID - kafka group id for Processor instances
### Provider
#### CASSANDRA_NODE - Cassandra host
#### CASSANDRA_PORT - Cassandra port


