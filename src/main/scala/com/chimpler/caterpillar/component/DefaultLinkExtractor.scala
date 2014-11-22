package com.chimpler.caterpillar.component

import com.chimpler.caterpillar.CrawlData
import com.chimpler.caterpillar.component.traits.LinkExtractor
import jodd.lagarto.dom.{LagartoDOMBuilder, NodeSelector}

import scala.collection.JavaConversions._

class DefaultLinkExtractor extends LinkExtractor {
  override def extractLinks(crawlData: CrawlData): Set[String] = {
    val domBuilder = new LagartoDOMBuilder()
    val doc = domBuilder.parse(crawlData.data)
    val nodeSelector = new NodeSelector(doc)
    nodeSelector.select("a[href]").map {
      node => node.getAttribute("href")
    }.toSet
  }
}
