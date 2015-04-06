package com.fiser.spray.versioning

import spray.http.HttpEntity
import spray.httpx.marshalling.Marshaller
import spray.httpx.unmarshalling.Unmarshaller
import com.fiser.spray.versioning.VersionMediaTypes._

object FooProtocolV2 {

  sealed trait FooActorMessage
  case class HelloFoo(hi: String, there: String) extends FooActorMessage
  case class FooResponse(helloBackTo: String) extends FooActorMessage

  import org.json4s.native.Serialization.{read, write => swrite}

  implicit val V2HelloUnmarshaller: Unmarshaller[FooProtocolV2.HelloFoo] =
    Unmarshaller[FooProtocolV2.HelloFoo](`application/vnd.ww.v2.foo+json`) {
      case HttpEntity.NonEmpty(contentType, data) =>
        read[HelloFoo](data.asString)
    }

  implicit val V2HelloMarshaller: Marshaller[FooProtocolV2.HelloFoo] =
    Marshaller.of[HelloFoo](`application/vnd.ww.v2.foo+json`) { (value, contentType, ctx) =>
      ctx.marshalTo(HttpEntity(contentType, swrite(value)))
    }
  implicit val V2HelloBackUnmarshaller: Unmarshaller[FooProtocolV2.FooResponse] =
    Unmarshaller[FooResponse](`application/vnd.ww.v2.foo+json`) {
      case HttpEntity.NonEmpty(contentType, data) =>
        read[FooResponse](data.asString)
    }

  implicit val V2HelloBackMarshaller: Marshaller[FooProtocolV2.FooResponse] =
    Marshaller.of[FooResponse](`application/vnd.ww.v2.foo+json`) { (value, contentType, ctx) =>
      ctx.marshalTo(HttpEntity(contentType, swrite(value)))
    }
}