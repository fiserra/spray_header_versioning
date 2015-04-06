package com.fiser.spray.versioning

import spray.http.{MediaType, MediaTypes}

object VersionMediaTypes {
  lazy val `application/vnd.ww.v2.foo+json` =
    MediaTypes.register(MediaType.custom("application/vnd.ww.v2.foo+json"))
  lazy val `application/vnd.ww.v1.foo+json` =
    MediaTypes.register(MediaType.custom("application/vnd.ww.v1.foo+json"))
}

