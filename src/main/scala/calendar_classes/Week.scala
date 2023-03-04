package calendar_classes
import calendar_classes.Calendar

class Week(calendar: Calendar, private val weekNum: Int, private val year: Int):
  private val events = calendar.getAllEvents.filter(x => x.getWeek == weekNum)
  
  def getYearNum = this.year

  def getWeekNum = this.weekNum
  
  def getEvents = this.events

  def addEvent(event: Event) = events.addOne(event)

  override def toString = s"Week $weekNum"
end Week