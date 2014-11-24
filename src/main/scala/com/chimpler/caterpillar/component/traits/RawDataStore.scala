package com.chimpler.caterpillar.component.traits

import com.chimpler.caterpillar.{CrawlUrl, CrawlData}

import scala.concurrent.{Future, ExecutionContext}

trait RawDataStore {
  def save(crawlData: CrawlData): Future[Unit]
  def get(crawlId: String, url: String): Future[Option[CrawlData]]
  def load(crawlId: String, url: String): Future[CrawlData]
  def exists(crawlId: String, url: String): Future[Boolean]
}
