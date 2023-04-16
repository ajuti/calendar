package gui_elements

import calendar_classes._
import calendar_classes.service._

def sameDay(eventInterval: Interval) = 
    eventInterval.start.getDayOfYear() == eventInterval.`end`.getDayOfYear() || eventInterval.`end`.getHour() == 0 && eventInterval.`end`.getMinute() == 0 && eventInterval.lengthInHours < 24
