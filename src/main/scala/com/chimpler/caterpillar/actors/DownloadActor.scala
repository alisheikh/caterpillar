package com.chimpler.caterpillar.actors

import akka.actor.{Props, Actor}
import akka.actor.Actor.Receive
import akka.routing.{RoundRobinRoutingLogic, Router, ActorRefRoutee}
import com.chimpler.caterpillar.CrawlUrl
import com.chimpler.caterpillar.util.HttpClient
import com.chimpler.caterpillar.component.{DefaultDownloader}
import scala.concurrent.ExecutionContext.Implicits.global

class DownloadActor extends Actor {
  val downloader = new DefaultDownloader()
  var router = {
    val routees = Vector.fill(5) {
      val r = context.actorOf(Props[ExtractActor])
      context watch r
      ActorRefRoutee(r)
    }
    Router(RoundRobinRoutingLogic(), routees)
  }

  override def receive: Receive = {
    case crawlUrl:CrawlUrl =>
      downloader.download(crawlUrl) map{
        crawlData => router.route(crawlData, sender())
      }

  }
}
