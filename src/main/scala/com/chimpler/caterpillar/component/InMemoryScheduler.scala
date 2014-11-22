package com.chimpler.caterpillar.component

import com.chimpler.caterpillar.CrawlUrl
import com.chimpler.caterpillar.components.traits.Scheduler

import scala.collection.mutable

class InMemoryScheduler extends Scheduler {
  val queue = mutable.Queue.empty[CrawlUrl]

  override def open(): Unit = {
    super.open()
  }

  override def queue(url: CrawlUrl) {
    queue += url
  }

  override def dequeue(): CrawlUrl = {
    return queue.dequeue()
  }

  override def close() {
    super.close()
  }
}
