package calendar_classes
import calendar_classes._
import calendar_classes.service._
import java.time.LocalDateTime
import scala.collection.mutable.Buffer

class Day(calendar: Calendar, private val interval: Interval):
  private val events = EventFilter.getDayEvents(calendar, getLdt)

  def updateEvents() = 
    events.clear()
    events.addAll(EventFilter.getDayEvents(calendar, getLdt))

  def getInterval = this.interval

  def getLdt: LocalDateTime = interval.start

  def getEvents: Buffer[Event] = this.events

  def addEvent(event: Event) = events.addOne(event)

  def deleteEvent(event: Event) = events.remove(events.indexOf(event))

  override def toString = s"${interval.start.getDayOfMonth} of ${interval.start.getMonth}"
end Day