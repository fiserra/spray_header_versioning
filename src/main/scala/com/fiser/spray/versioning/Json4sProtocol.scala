package com.fiser.spray.versioning

import org.json4s.{DefaultFormats, Formats}
import spray.httpx.Json4sSupport

class Json4sProtocol extends Json4sSupport {
  implicit def json4sFormats: Formats = DefaultFormats
}
