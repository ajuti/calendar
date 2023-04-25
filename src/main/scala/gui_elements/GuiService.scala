package gui_elements

import calendar_classes._
import calendar_classes.service._
import java.time._

def sameDay(eventInterval: Interval) = 
    eventInterval.start.getDayOfYear() == eventInterval.`end`.getDayOfYear() || eventInterval.`end`.getHour() == 0 && eventInterval.`end`.getMinute() == 0 && eventInterval.lengthInHours < 24

def if0then00(ldt: LocalDateTime, field: String): String =
    field match
        case "hour" =>
            if ldt.getHour() == 0 then "00" else ldt.getHour().toString()
        case "minute" =>
            if ldt.getMinute() == 0 then "00" else ldt.getMinute().toString()
        case _ => "00"
end if0then00
    