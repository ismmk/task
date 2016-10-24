package com.koval.proc

import org.json4s.JValue


/**
  * Created by Volodymyr Kovalenko
  */
case class EventEntry(
  userId : String,
  clientId : String,
  entryType : String,
  actionTime : Long,
  params : Map[String, JValue])

