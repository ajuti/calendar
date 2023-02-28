package calendar_classes
import calendar_classes.Calendar
import java.time.LocalDateTime

class Day(calendar: Calendar, private val day: LocalDateTime):
  private val events = calendar.getAllEvents.filter(x => x.getDay == day.getDayOfYear)

  def getEvents = this.events

  override def toString = s"${day.getDayOfMonth} of ${day.getMonth}"
end Day