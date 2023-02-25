package calendar_classes

import scala.collection.mutable.Buffer

class Calendar:
  private val events = Buffer[Event]()

  def getAllEvents = this.events

  def addEvent(event: Event) = events.addOne(event)

  def deleteEvent(event: Event) = events.remove(events.indexOf(event))

  def searchEvents(filter: String): Buffer[Event] =
    events.filter(x => x.getName == filter || x.getTags.contains(filter))
  end searchEvents


end Calendar

class Week(calendar: Calendar):
//  private val events = calendar.getAllEvents.filter(x => x.getTime

  def showNextWeek() = ???

  def showPreviousWeek() = ???

end Week

class Day:
  def showNextDay() = ???

  def showPreviousDay() = ???

end Day
