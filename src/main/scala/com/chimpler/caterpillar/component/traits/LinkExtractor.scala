package com.chimpler.caterpillar.component.traits

import com.chimpler.caterpillar.CrawlData

trait LinkExtractor {
  def extractLinks(crawlData: CrawlData): Set[String]
}
