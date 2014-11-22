package com.chimpler.caterpillar

case class StartCrawlMessage(crawlId: String)
case class StopCrawlMessage(crawlId: String)
case class UrlDownloadedMessage(url: String)