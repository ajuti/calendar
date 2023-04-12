package calendar_classes

import calendar_classes.service._
import calendar_classes._

import java.time.LocalDateTime
import scala.collection.mutable.Buffer
import scala.compiletime.ops.int
import scalafx.collections.ObservableBuffer

class Calendar:
  private val events = new ObservableBuffer ++ new FileReader("events.csv").addAllEvents() // GenAllEventsFromFile.get // stores all events of calendar
  
  // mutable variable for tracking day | Day object knows which events are scheduled for itself
  private var currentDay = Day(this, DateAndWeekGen().genDayInterval(LocalDateTime.now()))
  
  // mutable variable for tracking week | Week object also knows off of the events scheduled for that week
  private var currentWeek = Week(this, DateAndWeekGen().genWeekInterval(currentDay.getLdt))
  
  // Returns the index of the week (1 to 52 or 53 depending on leap years)
  // Utilizes GetWeek -serviceobject, which calculates week number, given the LocalDateTime
  def getWeekIndex = GetWeek.getWeek2(currentWeek.getInterval.start)

  def getCurrentDay = this.currentDay

  def getCurrentDate: LocalDateTime = currentDay.getLdt

  def getCurrentWeek: Week = this.currentWeek

  def getAllEvents: Buffer[Event] = this.events

  // Sets new Day and Week objects to the mutable variables keeping track of said values
  def setDayAndWeek(date: LocalDateTime) = 
    val gen = new DateAndWeekGen()
    this.currentDay = Day(this, gen.genDayInterval(date))
    this.currentWeek = Week(this, gen.genWeekInterval(currentDay.getLdt))
  end setDayAndWeek

  // Adds new event to list of all events as well as the current Day and Week if event is scheduled for current Day/Week
  def addEvent(event: Event): Unit = 
    events.addOne(event)
    if event.getInterval.intersects(currentDay.getInterval) then
      currentDay.addEvent(event)
    if event.getInterval.intersects(currentWeek.getInterval) then
      currentWeek.addEvent(event)
  end addEvent

  // Deletes events from calendar and current Day/Week
  def deleteEvent(event: Event) = 
    events.remove(events.indexOf(event))
    if event.getInterval.intersects(currentDay.getInterval) then
      currentDay.deleteEvent(event)
    if event.getInterval.intersects(currentWeek.getInterval) then
      currentWeek.deleteEvent(event)
  end deleteEvent

  // Filtering methods for string input and tag input
  // String, will give all events whose title contains or tags contain input filter

  // ByTags will only search with given tag:
  // E.g. given tag: Work, it will only show events tagged with Work, not events that
  // have "Work" in their title, but not Work as a tag 
  def searchEvents(filter: String): Buffer[Event] =
    events.filter(x => x.getName.contains(filter) || x.getTags.contains(filter) || x.getInfo.contains(filter))
  end searchEvents

  def searchByTags(tag: Tag): Buffer[Event] =
    events.filter(x => x.getTags.contains(tag.tagName))
  end searchByTags

  // Updates the internal state of calendar
  def showNextWeek() =
    val gen = new DateAndWeekGen()
    currentWeek = gen.newWeek(this, currentWeek.getInterval, 7)
    currentDay =  gen.newDay(this, 7)
  end showNextWeek

  def showPreviousWeek() = 
    val gen = new DateAndWeekGen()
    currentWeek = gen.newWeek(this, currentWeek.getInterval, -7)
    currentDay = gen.newDay(this, -7)
  end showPreviousWeek

  def showNextDay() = 
    val gen = new DateAndWeekGen()
    currentDay = gen.newDay(this, 1)
    if !currentWeek.getInterval.contains(currentDay.getLdt) then
      currentWeek = gen.newWeek(this, currentWeek.getInterval, 7)
  end showNextDay

  def showPreviousDay() = 
    val gen = new DateAndWeekGen()
    currentDay = gen.newDay(this, -1)
    if !currentWeek.getInterval.contains(currentDay.getLdt) then
      currentWeek = gen.newWeek(this, currentWeek.getInterval, -7)
  end showPreviousDay

  def upload() = 
    val fileOut = FileWriter("events.csv", this.events)
    fileOut.writeAllEvents()

end Calendar
