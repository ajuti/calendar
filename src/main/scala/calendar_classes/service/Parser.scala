package calendar_classes.service

import calendar_classes._
import scala.collection.mutable.Buffer
import java.time._
import scalafx.scene.paint.Color

class Parser():

    def eventToListOfStrings(event: Event): List[String] = 
        List[String](event.getName, event.getTime, event.getTags, event.getInfo, event.getColorString)
    end eventToListOfStrings

    def toEventFromString(line: String): Event =
        // name, time, stringTags, extra, color
        val elements = line.split(",")
        val name = elements.head
        val start = LocalDateTime.parse(elements(1).takeWhile(_ != ';'))
        val end = LocalDateTime.parse(elements(1).dropWhile(_ != ';').tail)
        val stringTags = elements(2)
        val extra = elements(3)
        val colorElems = elements(4)
        val color: Option[Color] = 
            if colorElems != "!empty!" then 
                val rgb = colorElems.split("-").map(_.toLong)
                Some(Color(rgb(0), rgb(1), rgb(2), 1))
            else None

        Event(name, Interval(start, `end`), stringTags, extra, color)
    end toEventFromString