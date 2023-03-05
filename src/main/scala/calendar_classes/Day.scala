package calendar_classes
import calendar_classes.Calendar
import java.time.LocalDateTime
import scala.collection.mutable.Buffer

class Day(calendar: Calendar, private val date: LocalDateTime):
  private val events = calendar.getAllEvents.filter(x => x.getDay == date.getDayOfYear)

  def getLdt: LocalDateTime = this.date

  def getEvents: Buffer[Event] = this.events

  def addEvent(event: Event) = events.addOne(event)

  def deleteEvent(event: Event) = events.remove(events.indexOf(event))

  override def toString = s"${date.getDayOfMonth} of ${date.getMonth}"
end Day