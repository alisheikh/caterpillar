package com.chimpler.caterpillar

import org.joda.time.DateTime

case class CrawlUrl(url: String, crawlId: String, headers: Map[String, List[String]] = Map.empty[String, List[String]], referrer: Option[String] = None, retries: Int = 0)

case class CrawlData(crawlUrl: CrawlUrl, statusCode: Int, data: String, headers: Map[String, List[String]] = Map.empty)

case class RawPage(id: String, url: String)

case class Crawl(crawlId: String, name: String, homepage: String, rootUrls: Set[String], created: DateTime, updated: DateTime)

// TODO: maybe add things like ALT and link content