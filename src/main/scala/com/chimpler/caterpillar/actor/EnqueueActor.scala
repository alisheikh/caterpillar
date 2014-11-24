package com.chimpler.caterpillar.actor

import java.io.File
import java.nio.charset.Charset
import java.util.Properties

import akka.actor.{Props, Actor}
import akka.actor.Actor.Receive
import com.chimpler.caterpillar.CrawlUrl
import com.chimpler.caterpillar.component.MongoRawDataStore
import com.google.common.base.Charsets
import com.google.common.hash.{PrimitiveSink, Funnel, Funnels, BloomFilter}
import kafka.producer.{ProducerConfig, KeyedMessage, Producer}
import org.mapdb.DBMaker
import scala.collection.JavaConversions._
import scala.concurrent.Future
import scala.util.Success
import scala.collection.mutable

class EnqueueActor extends Actor {
  import context.dispatcher

  // TODO: load it from config
  val props = new Properties()
  props ++= Map(
    "metadata.broker.list" -> "127.0.0.1:9093",
    "serializer.class" -> "com.chimpler.caterpillar.CrawlUrlEncoder"
  )
  val crawlId = "marcustroy"
  val kafkaConfig = new ProducerConfig(props)
  val producer = new Producer[String, CrawlUrl](kafkaConfig)
  val dequeueActor = context.actorOf(Props(classOf[DequeueActor]), "dequeueActor")

  val crawledLinkSet = new CrawledLinkSet()

  val rawPageStore = new MongoRawDataStore()
  import context.dispatcher

  val producerMap = mutable.Map.empty[String, Producer[String, CrawlUrl]]

  // TODO: use cuckoo filter instead of bloom filter

  override def receive: Receive = {
    case crawlUrl: CrawlUrl =>
      crawledLinkSet.existsAndPut(crawlUrl)
  }

  class CrawledLinkSet {
    val bloomFilter = BloomFilter.create(new CrawlUrlFunnel(), 1000);
    val tempFile = File.createTempFile("caterpillar", ".mapdb")
    val fileMap = DBMaker.newTempHashSet[String]()

    def existsAndPut(crawlUrl: CrawlUrl): Future[Boolean] = {
      if (bloomFilter.mightContain(crawlUrl)) {
        // hit the cache
        if (fileMap(crawlUrl.url)) {
          Future.successful(false)
        } else {
          // hit the datastore
          rawPageStore.exists(crawlId, crawlUrl.url).andThen {
            case Success(true) =>
              bloomFilter.put(crawlUrl)
              producer.send(new KeyedMessage[String, CrawlUrl](crawlId, crawlUrl))
          }
        }
      } else {
        Future.successful(false)
      }
    }

    class CrawlUrlFunnel extends Funnel[CrawlUrl] {
      override def funnel(crawlUrl: CrawlUrl, sink: PrimitiveSink) {
        sink.putString(crawlUrl.url, Charsets.UTF_8)
      }
    }
  }
}