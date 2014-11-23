package com.chimpler.caterpillar.actors

import akka.actor.{ActorLogging, ActorRef, Props, Actor}
import akka.routing.{FromConfig}
import com.chimpler.caterpillar._
import com.sclasen.akka.kafka.{StreamFSM, AkkaConsumer, CommitConfig, AkkaConsumerProps}
import kafka.serializer.StringDecoder

import scala.collection.mutable
import scala.concurrent.Future

class DequeueActor extends Actor with ActorLogging {
  val activeCrawls = mutable.Set.empty[String]
  lazy val downloadRouter = context.system.actorOf(FromConfig.props(Props[DownloadActor]), "router_download")

  def startKafkaActor(crawlId: String): Future[Unit] = {
    if (!activeCrawls(crawlId)) {
      activeCrawls += crawlId
      log.info(s"Starting crawl $crawlId")
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
    } else {
      Future.successful()
    }
  }

  override def receive = {
    case StartCrawlMessage(crawlId) =>
      startKafkaActor(crawlId)

    case crawlUrl: CrawlUrl =>
      sender() ! StreamFSM.Processed
      // TODO: maybe forward and have the extractor ack
      downloadRouter ! crawlUrl
  }
}

object DequeueActor {
  val GroupId = "caterpillar-group"
}