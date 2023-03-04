package calendar_classes
import calendar_classes.Calendar
import java.time.LocalDateTime

class Day(calendar: Calendar, private val date: LocalDateTime):
  private val events = calendar.getAllEvents.filter(x => x.getDay == date.getDayOfYear)

  def getLdt = this.date

  def getEvents = this.events

  def addEvent(event: Event) = events.addOne(event)

  override def toString = s"${date.getDayOfMonth} of ${date.getMonth}"
end Day