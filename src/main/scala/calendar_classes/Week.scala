package calendar_classes
import calendar_classes.Calendar
import scala.collection.mutable.Buffer

class Week(calendar: Calendar, private val weekNum: Int, private val year: Int):
  private val events = calendar.getAllEvents.filter(x => x.getWeek == weekNum)
  
  def getYearNum: Int = this.year

  def getWeekNum: Int = this.weekNum
  
  def getEvents: Buffer[Event] = this.events

  def addEvent(event: Event) = events.addOne(event)

  def deleteEvent(event: Event) = events.remove(events.indexOf(event))

  override def toString = s"Week $weekNum"
end Week