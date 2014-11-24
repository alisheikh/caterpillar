package com.chimpler.caterpillar.actor

import akka.actor.{ActorLogging, Actor}
import com.chimpler.caterpillar.CrawlData
import com.chimpler.caterpillar.component.DefaultLinkExtractor

class ExtractActor extends Actor with ActorLogging {
  val linkExtractor = new DefaultLinkExtractor()

  override def receive: Receive = {
    case crawlData: CrawlData =>
      val links = linkExtractor.extractLinks(crawlData)
      log.info(links.mkString(", "))
  }
}
