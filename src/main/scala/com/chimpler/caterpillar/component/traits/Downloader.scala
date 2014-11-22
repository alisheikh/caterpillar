package com.chimpler.caterpillar.component

import com.chimpler.caterpillar.{CrawlData, CrawlUrl}

import scala.concurrent.Future
import scala.io.Source

trait Downloader {
  def download(crawlUrl: CrawlUrl): Future[CrawlData]
}
