package com.chimpler.caterpillar.actors

import akka.actor.Actor
import com.chimpler.caterpillar.CrawlData
import com.chimpler.caterpillar.component.DefaultLinkExtractor

class ExtractActor extends Actor {
  val linkExtractor = new DefaultLinkExtractor()

  override def receive: Receive = {
    case crawlData: CrawlData =>
      val links = linkExtractor.extractLinks(crawlData)
      println(links)
  }
}
