package calendar_classes
import calendar_classes.Calendar

class Week(calendar: Calendar, private val weekNum: Int, private val year: Int):
  private val events = calendar.getAllEvents.filter(x => x.getWeek == weekNum)

  def getEvents = this.events

  override def toString = s"Week $weekNum"
end Week