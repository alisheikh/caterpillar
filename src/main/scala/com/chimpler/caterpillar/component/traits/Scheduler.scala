package com.chimpler.caterpillar.components.traits

import com.chimpler.caterpillar.CrawlUrl

trait Scheduler {
  def open() {}
  def close() {}

  def queue(url: CrawlUrl)
  def dequeue(): CrawlUrl
}
