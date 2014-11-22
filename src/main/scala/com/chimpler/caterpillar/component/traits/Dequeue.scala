package com.chimpler.caterpillar.component.traits

import com.chimpler.caterpillar.CrawlUrl

trait Dequeue {
  def receive(crawlId: String): CrawlUrl
}
