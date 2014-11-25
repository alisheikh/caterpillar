package com.chimpler.caterpillar.component

import com.chimpler.caterpillar.CrawlData
import com.chimpler.caterpillar.component.traits.LinkExtractor
import io.mola.galimatias.URL
import jodd.lagarto.dom.{LagartoDOMBuilder, NodeSelector}

import scala.collection.JavaConversions._

class DefaultLinkExtractor extends LinkExtractor {
  override def extractLinks(crawlData: CrawlData): Set[String] = {
    val domBuilder = new LagartoDOMBuilder()
    domBuilder.enableHtmlPlusMode()
    val doc = domBuilder.parse(crawlData.data)
    val nodeSelector = new NodeSelector(doc)
    nodeSelector.select("a[href]").map {
      node =>
        val baseUrl = URL.parse(crawlData.crawlUrl.url)
        val url = URL.parse(baseUrl, node.getAttribute("href"))
        url.toHumanString
    }.toSet
  }
}
