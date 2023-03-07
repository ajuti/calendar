package calendar_classes.service

import scala.math._
import calendar_classes._
import java.time._

object EventFilter:

    def getWeekEvents(calendar: Calendar, week: Interval) = 
        calendar.getAllEvents.filter(
            x => x.getInterval.intersects(week)
            )
    end getWeekEvents

    def getDayEvents(calendar: Calendar, date: LocalDateTime) = 
        calendar.getAllEvents.filter(
            x => x.getInterval.contains(date)
            )
    end getDayEvents

end EventFilter


     
     
     
        