package com.chimpler.caterpillar.store.traits

import java.io.InputStream

import com.chimpler.caterpillar.CrawlUrl

import scala.concurrent.Future

trait RawPageStore {
  def save(crawlUrl: CrawlUrl, content: String)
  def getHeader(url: String): CrawlUrl
  def getContent(url: String): String
}
