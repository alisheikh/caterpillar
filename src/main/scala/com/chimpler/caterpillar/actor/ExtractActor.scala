package com.chimpler.caterpillar.actor

import akka.actor.{Actor, ActorLogging, Props}
import com.chimpler.caterpillar.{CrawlUrl, CrawlData}
import com.chimpler.caterpillar.component.DefaultLinkExtractor

class ExtractActor extends Actor with ActorLogging {
  val linkExtractor = new DefaultLinkExtractor()
  val enqueueActor = context.actorOf(Props[EnqueueActor])

  override def receive: Receive = {
    case crawlData: CrawlData =>
      val links = linkExtractor.extractLinks(crawlData)
      val crawlUrls = links.map {
        link => CrawlUrl(link, crawlData.crawlUrl.crawlId)
      }
      enqueueActor ! crawlUrls
  }
}
