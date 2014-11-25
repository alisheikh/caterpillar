package com.chimpler.caterpillar.component

import com.chimpler.caterpillar.component.traits.RawDataStore
import com.chimpler.caterpillar.{CrawlData, CrawlUrl}
import reactivemongo.api.MongoDriver
import reactivemongo.api.collections.default.BSONCollection
import reactivemongo.api.gridfs.GridFS
import reactivemongo.bson._

import scala.concurrent.{ExecutionContext, Future}

class MongoRawDataStore(implicit ec: ExecutionContext) extends RawDataStore {

  implicit val mapReader = new BSONDocumentReader[Map[String, List[String]]] {
    def read(bson: BSONDocument): Map[String, List[String]] = {
      bson.elements map {
        // TODO: fix this read
        case (key, valueList: BSONArray) => key -> valueList.values.map {
          case value: BSONString => value.value
        }.toList
      }
    }.toMap
  }

  implicit val mapWriter = new BSONDocumentWriter[Map[String, List[String]]] {
    def write(map: Map[String, List[String]]): BSONDocument = {
      BSONDocument(map.toStream.map {
        case (key, valueList: List[String]) => key -> BSONArray(valueList.map(BSONString))
      })
    }
  }

  implicit val crawlUrlWriter = Macros.handler[CrawlUrl]
  implicit val crawlUrlReader: BSONDocumentReader[CrawlUrl] = Macros.reader[CrawlUrl]
  implicit val crawlDataWriter = Macros.handler[CrawlData]
  implicit val crawlDataReader: BSONDocumentReader[CrawlData] = Macros.reader[CrawlData]

  val driver = new MongoDriver()
  val connection = driver.connection(List("localhost"))

  // TODO: make it configurable
  val db = connection("caterpillar")
  val gfs = GridFS(db)
  val collection = db[BSONCollection]("raw_pages")

  override def save(crawlData: CrawlData): Future[Unit] = {
    collection.save(crawlData).map(_ => Unit)
  }

  override def get(crawlId: String, url: String): Future[Option[CrawlData]] = {
    collection.find(BSONDocument("crawlId" -> crawlId)).one[CrawlData]
  }

  override def load(crawlId: String, url: String): Future[CrawlData] = {
    get(crawlId, url) map {
      case Some(crawlData) => crawlData
      case _ => throw new RuntimeException(s"Crawl data for crawlId=$crawlId does not exist")
    }
  }

  override def exists(crawlId: String, url: String): Future[Boolean] = {
    collection.find(BSONDocument("crawlId" -> crawlId)).projection(BSONDocument()).one map {
      case Some(_) => true
      case _ => false
    }
  }
}
