package com.koval.proc

import kafka.serializer.Decoder
import kafka.utils.VerifiableProperties
import org.json4s._
import org.json4s.jackson.JsonMethods._

/**
  * Created by Volodymyr Kovalenko
  */
class EventEntryDecoder(props: VerifiableProperties = null) extends Decoder[EventEntry] {
  implicit val formats = DefaultFormats

  val encoding =
    if(props == null)
      "UTF8"
    else
      props.getString("serializer.encoding", "UTF8")

  def fromBytes(bytes: Array[Byte]): EventEntry = {
    parse(new String(bytes, encoding)).extract[EventEntry]
  }
}
