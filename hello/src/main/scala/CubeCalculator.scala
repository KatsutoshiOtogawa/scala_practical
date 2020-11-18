import java.net.UnknownHostException

// import com.mongodb.MongoClient
// import com.mongodb.MongoClientURI
// import com.mongodb.ServerAddress

// import com.mongodb.client.MongoDatabase
// import com.mongodb.client.MongoCollection

// import org.bson.Document
// import com.mongodb.Block

// import com.mongodb.client.MongoCursor
// import com.mongodb.client.model.Filters._
// import com.mongodb.client.result.DeleteResult
// import com.mongodb.client.model.Updates._
// import com.mongodb.client.result.UpdateResult

import org.mongodb.scala._
import org.mongodb.scala.model.Aggregates._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Projections._
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.model.Updates._
import org.mongodb.scala.model._
import org.mongodb.scala.MongoClient._

import java.util.Properties;
import java.io.FileNotFoundException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.charset.StandardCharsets;
import java.io.IOException;
import java.io.Reader;
import java.io.File;
import java.io.InputStream;
import java.net.URI;
import java.io.InputStreamReader;
import java.io.FileInputStream;

// import java.util.ArrayList
// import java.util.List

import scala.collection.immutable.Seq;
import scala.collection.immutable.ArraySeq;
import scala.collection.immutable.Map;
import scala.collection.immutable.HashMap;
import scala.collection.immutable.Set;
import scala.collection.immutable.HashSet;

import scala.io.Source;

import collection.JavaConverters._
import org.bson.Document;

import scala.util.Using


object CubeCalculator extends App {

  private def createConnection(user: String,pass: String,host: String,port: String = "27017",dbname: String): MongoClient = {
    return new MongoClient(
      com.mongodb.reactivestreams.client.MongoClients.create(s"mongodb://$user:$pass@$host:$port/$dbname")
    )
  }

  // def createConnection(connectionProperties: Map[String,String]): MongoClient = {

  //   val user: String = connectionProperties.get("user").get
  //   val pass: String = connectionProperties.get("pass").get
  //   val host: String = connectionProperties.get("host").get
  //   val port: String  = connectionProperties.get("port").getOrElse(default = null)
  //   val dbname: String = connectionProperties.get("dbname").get

  //   return createConnection(
  //     user = user
  //     ,pass = pass
  //     ,host = host
  //     ,port = port
  //     ,dbname = dbname
  //   )
  // }

  def createConnection(connectionProperties: Map[String,String]): Option[MongoClient] = {

    try
    {
        val user: String = connectionProperties.get("user").get
        val pass: String = connectionProperties.get("pass").get
        val host: String = connectionProperties.get("host").get
        val port: String  = connectionProperties.get("port").getOrElse(default = null)
        val dbname: String = connectionProperties.get("dbname").get
        return Some(createConnection(
          user = user
          ,pass = pass
          ,host = host
          ,port = port
          ,dbname = dbname
        ))
    }catch{
        case ex: Exception => return None
    }
  }
  /**
    * operation is not separated by exception,because of use Option Type.
    *
    * @param uri
    * @return
    */
  def getConnectionProperties(uri: URI) :Option[Map[String,String]] = {

    var result: Option[Map[String,String]] = None

    // def f1 = (v1:URI) => new FileInputStream(new File(v1))
    // def f2 = (v1:FileInputStream,v2:String) => new InputStreamReader(v1,v2)
    // def f3 = (v1:InputStreamReader,v2:Properties) => {
    //   try 
    //   {
    //     v2.load(v1)
    //     return Some(Map[String,String](
    //         "user" -> properties.getProperty("user")
    //         , "pass" -> properties.getProperty("pass")
    //         , "host" -> properties.getProperty("host")
    //         , "port" -> properties.getProperty("port")
    //         , "dbname" -> properties.getProperty("dbname")
    //     ))
    //   }catch{
    //     case ex:Exception => {
    //       println(ex)
    //       return None
    //     }
    //   }
    // }
    // val fx = (
    //           f1
    //   andThen f2 (v2 = "UTF-8")
    //   andThen f3 (v2 = new Properties())
    // )

    Using(new InputStreamReader(new FileInputStream(new File(uri)),"UTF-8")){ fp =>

      val properties: Properties  = new Properties()

      try
      {
          properties.load(fp);

          result = Some(Map[String,String](
            "user" -> properties.getProperty("user")
            , "pass" -> properties.getProperty("pass")
            , "host" -> properties.getProperty("host")
            , "port" -> properties.getProperty("port")
            , "dbname" -> properties.getProperty("dbname")
          ))
          println("create Connection")
      }catch{

        case ex:Exception => {
          println(ex)
          // return None
        }
        // logger.error("openProperties message [%s]",ex.getMessage());
        // throw ex;
      }

    }

    return result
  }

  def main(args: Seq[String]) :Unit = {
    // val mongoClient: MongoClient = MongoClient(
    // MongoClientSettings.builder()
    // .applyToClusterSettings((builder: ClusterSettings.Builder) => builder.hosts(List(new ServerAddress("hostOne")).asJava))
    // .build())
// [Scala MongoDb document](http://mongodb.github.io/mongo-java-driver/4.1/apidocs/mongo-scala-driver/index.html)
// [Scala Mongodb reactivesteams doc](https://mongodb.github.io/mongo-java-driver-reactivestreams/1.13/javadoc/)


    // val connectionProperties: Map[String,String] = getConnectionProperties(
    //   new URI(
    //       Paths.get(System.getProperty("user.dir"),"resources","connection.properties").toString()
    //     )
    //   ).get

    def f1 = (v1: String) => v1
    def f2 = (v1: String) => Paths.get(v1,"resources","connection.properties").toString()
    def f3 = (v1: String) => new URI(v1)
    def f4 = (v1: URI) => getConnectionProperties(v1).get
    def f5 = (v1: Map[String,String]) => {
      createConnection(v1) match {
        case None => sys.exit(1)
        case Some(value) => value
      }
    }
    def f6 = (v1: MongoClient) => v1.close()

    val fx = (
                f1
        andThen f2
        andThen f3
        andThen f4
        andThen f5
        andThen f6
    )
    println("Hello World")
    fx(System.getProperty("user.dir"))

    // val connection: MongoClient = createConnection(
    //   user = connectionProperties.get("user").get
    //   ,pass = connectionProperties.get("pass").get
    //   ,host = connectionProperties.get("host").get
    //   ,port = connectionProperties.get("port").get
    //   ,dbname = connectionProperties.get("dbname").get
    // )

    // val seedData: Seq[Document] = List[Document](
    //   new Document("decade", "1970s")
    //         .append("artist", "Debby Boone")
    //         .append("song", "You Light Up My Life")
    //         .append("weeksAtOne", 10)
    // )


    // connection.close()

//     scala> def s1 = Integer.parseInt(_: String)
// scala> def s2 = Math.abs _
// scala> def s3 = (a: Int) => a + 1

// scala> def f2 = s1.andThen(s2).andThen(s3)

    
  }
  //   val document: Document = new Document("decade", "1970s")
  //           .append("artist", "Debby Boone")
  //           .append("song", "You Light Up My Life")
  //           .append("weeksAtOne", 10)
  //   seedData.add(document)
  // }
}