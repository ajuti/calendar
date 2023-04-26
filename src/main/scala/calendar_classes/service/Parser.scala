package calendar_classes.service

import calendar_classes._
import scala.collection.mutable.Buffer
import java.time._
import scalafx.scene.paint.Color


case class IllegalEventFormat(description: String) extends Exception(description)

class Parser():

    def eventToListOfStrings(event: Event): List[String] = 
        List[String](event.getName, event.getTime, event.getTags, event.getInfo, event.getColorString)
    end eventToListOfStrings

    def toEventFromString(line: String): Event =
        val elements = line.split(",")
        if elements.size != 5 then throw IllegalEventFormat("Event read from CSV file is corrupted, skipping")
        val name = elements.head
        val start = LocalDateTime.parse(elements(1).takeWhile(_ != ';')).withNano(0)
        val end = LocalDateTime.parse(elements(1).dropWhile(_ != ';').tail).withNano(0)
        val stringTags = elements(2)
        val extra = elements(3)
        val colorElems = elements(4)
        val color: Option[Color] = 
            if colorElems != "!empty!" then 
                val rgb = colorElems.split("-").map(_.toDouble)
                Some(Color(rgb(0), rgb(1), rgb(2), 1))
            else None

        Event(name, Interval(start, `end`), stringTags, extra, color)
    end toEventFromString