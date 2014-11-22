package com.chimpler.caterpillar.actors

import akka.actor.{Props, Actor}
import akka.routing.{RoundRobinRoutingLogic, Router, ActorRefRoutee}
import com.chimpler.caterpillar._
import com.sclasen.akka.kafka.{StreamFSM, AkkaConsumer, CommitConfig, AkkaConsumerProps}
import grizzled.slf4j.Logging
import kafka.serializer.StringDecoder

import scala.collection.mutable

class DequeueActor extends Actor with Logging {
  val activeCrawls = mutable.Set.empty[String]

  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[DownloadActor])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  def startKafkaActor(crawlId: String) = {
    if (!activeCrawls(crawlId)) {
      info(s"Starting crawl $crawlId")
      activeCrawls += crawlId
      val consumerProps = AkkaConsumerProps.forContext[String, CrawlUrl](
        context = context,
        zkConnect = "localhost:2181",
        topic = crawlId,
        group = DequeueActor.GroupId,
        streams = 1,
        keyDecoder = new StringDecoder(),
        msgDecoder = new CrawlUrlDecoder(),
        receiver = self,
        commitConfig = new CommitConfig())
      val consumer = new AkkaConsumer(consumerProps)
      consumer.start()
    }
  }

  override def receive = {
    case StartCrawlMessage(crawlId) =>
      startKafkaActor(crawlId)

    case crawlUrl: CrawlUrl =>
      println(s"got url $crawlUrl")
      router.route(crawlUrl, sender())
      sender ! StreamFSM.Processed
  }
}

object DequeueActor {
  val GroupId = "caterpillar-group"
}