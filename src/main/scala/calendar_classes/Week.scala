package calendar_classes
import calendar_classes.Calendar
import calendar_classes.service._
import scala.collection.mutable.Buffer

class Week(calendar: Calendar, interval: Interval):
  private val events = EventFilter.getWeekEvents(calendar, interval)
  
  def getYearNum: Int = interval.start.getYear()

  def getInterval = this.interval

  def getWeekNum: Int = GetWeek.getWeek2(interval.start)
  
  def getEvents: Buffer[Event] = this.events

  def addEvent(event: Event) = events.addOne(event)

  def deleteEvent(event: Event) = events.remove(events.indexOf(event))

  override def toString = s"Week $getWeekNum"
end Week