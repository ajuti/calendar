package calendar_classes.service

import calendar_classes._
import scala.collection.mutable.Buffer
import java.time._
import java.awt.Color

class Parser():

    def toStringForm(events: Buffer[Event]): String = 
        var parsedString = ""
        for c <- events do
            parsedString = parsedString + s"${c.getName},${c.getTime},${c.getTags},${c.getInfo},${c.getColorString}\n"
        parsedString
    end toStringForm

    def toEventFromString(line: String): Event =
        // name, time, stringTags, extra, color
        val elements = line.split(",")
        val name = elements.head
        val start = LocalDateTime.parse(elements(1).takeWhile(_ != ';'))
        val end = LocalDateTime.parse(elements(1).dropWhile(_ != ';').tail)
        val stringTags = elements(2)
        val extra = elements(3)
        val colorElems = elements(4).split('-').map(_.toInt)
        val color: Option[Color] = 
            if colorElems.nonEmpty then Some(Color(colorElems(0), colorElems(1), colorElems(2)))
            else None

        Event(name, Interval(start, `end`), stringTags, extra, color)
    end toEventFromString