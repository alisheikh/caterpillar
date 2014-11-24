package com.chimpler.caterpillar

import java.util.Properties

import akka.actor.{Props, ActorSystem}
import com.chimpler.caterpillar.actor.DequeueActor
import com.typesafe.config.ConfigFactory
import kafka.producer.{KeyedMessage, Producer, ProducerConfig}
import scala.collection.JavaConversions._

object CrawlerApp extends App {
  val config = ConfigFactory.load("application.conf")
  val system = ActorSystem("caterpillar", config)

  // TODO: move it somewhere else
  val props = new Properties()
  props ++= Map(
    "metadata.broker.list" -> "127.0.0.1:9093",
    "serializer.class" -> "com.chimpler.caterpillar.CrawlUrlEncoder"
  )
  val crawlId = "marcustroy"
  val kafkaConfig = new ProducerConfig(props)
  val producer = new Producer[String, CrawlUrl](kafkaConfig)
  producer.send(new KeyedMessage[String, CrawlUrl](crawlId, CrawlUrl("http://www.marcustroy.com", "marcustroy", referrer = Option("test"))))
  val dequeueActor = system.actorOf(Props(classOf[DequeueActor]), "dequeueActor")
  dequeueActor ! StartCrawlMessage("marcustroy")
}
