package com.koval.proc

import com.datastax.driver.core.Session
import com.datastax.spark.connector.cql.CassandraConnector
import com.datastax.spark.connector.streaming.toDStreamFunctions
import com.typesafe.scalalogging.Logger
import kafka.serializer.StringDecoder
import org.apache.spark._
import org.apache.spark.storage.StorageLevel
import org.apache.spark.streaming._
import org.apache.spark.streaming.kafka.KafkaUtils

object Processor {

  val logger = Logger("Processor")

  def initConnection(sparkConf: SparkConf) = {
    val connector = CassandraConnector.apply(sparkConf)
    var session:Session = null
    do{
      try{
        // TODO db migration
        session = connector.openSession()
        session.execute("CREATE KEYSPACE IF NOT EXISTS access_log WITH  REPLICATION = {'class' : 'SimpleStrategy', 'replication_factor' : 1};")
        session.execute("CREATE TABLE IF NOT EXISTS access_log.entry (user_id text,client_id text,entry_type text, action_time timestamp,params map<text,text>, primary key(entry_type, client_id, user_id, action_time));")
      } catch {
        case t : Throwable => logger.warn("Can't connect to storage " , t)
      }
      Thread.sleep(2000)
    } while (session == null)
  }

  def main(args: Array[String]): Unit = {

    val cassandraHost: String = sys.env("CASSANDRA_HOST")
    val cassandraPort: String = sys.env("CASSANDRA_PORT")
    val zookeeperHosts: String = sys.env("ZOOKEEPER_HOSTS")
    val kafkaGroupId: String = sys.env("KAFKA_GROUP_ID")
    val cassandraKeyStore: String = sys.env("CASSANDRA_KEY_STORE")

    val sparkConf = new SparkConf().setAppName("LogToCasProcessor")
      .setMaster("local[2]")
      .set("spark.executor.memory","1g")
      .set("spark.cassandra.connection.host", cassandraHost)
      .set("spark.cassandra.connection.port", cassandraPort)
      .set("spark.cassandra.connection.timeout_ms", "60000")

    val ssc = new StreamingContext(sparkConf, Seconds(2))

    initConnection(sparkConf)


    val kafkaParams = Map[String, String](
      "zookeeper.connect" -> zookeeperHosts, "group.id" ->  kafkaGroupId,
      "zookeeper.connection.timeout.ms" -> "30000")

    val kafkaStream = KafkaUtils.createStream[String, EventEntry, StringDecoder, EventEntryDecoder] (ssc, kafkaParams, Map("access_log" -> 1), StorageLevel.MEMORY_ONLY)
    ssc.checkpoint("checkpoint")

    kafkaStream.map(_._2).saveToCassandra(cassandraKeyStore, "entry")

    ssc.start()
    ssc.awaitTermination()
  }
}
