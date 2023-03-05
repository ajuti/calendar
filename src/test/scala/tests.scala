import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import calendar_classes.{Calendar, ColorTag, Day, Event, Week, Tag}
import calendar_classes.service._
import java.time.*

class EventTest extends AnyFlatSpec, Matchers:
  val startingTime = LocalDateTime.of(2023, 3, 27, 10, 0)
  val endingTime = LocalDateTime.of(2023, 1, 1, 16, 30)
  val subject = Event("work", startingTime, endingTime)

  "setColor" should "assign a ColorTag to an event" in {
    subject.setColor(ColorTag(10,10,10))
    assert(subject.getColor.nonEmpty)
    println(subject.getColor)
  }
  "removeColor" should "remove a color from bannerColor" in {
    subject.removeColor()
    assert(subject.getColor.isEmpty)
  }
  "subject.tags" should "be empty if no tags have been given for initialization of the object" in {
    assert(subject.getTags.isEmpty)
  }
  "addTag()" should "add a string to buffer of all tags" in {
    subject.addTag("school")
    subject.addTag("gym")
  }
  "removeTag" should "remove tags if possible" in {
    subject.removeTag("gym")
    subject.removeTag("xdd")
    println(subject.getTags)
  }
  "getTags" should "return all tags as a string" in {
    println(subject.getTags)
  }
  "addInfo and getInfo" should "add additional info to the event, and get it in a string" in {
    subject.addInfo("extra info about the event")
    println(subject.getInfo)
  }
  "getTime" should "return given event's timeframe" in {
    println(subject.getTime)
  }
  "getWeek" should "return correct week in integer" in {
    println(subject.getWeek)
  }
end EventTest


class CalendarTest extends AnyFlatSpec, Matchers:
  val subject = Calendar()
  val startingTime = LocalDateTime.of(2023, 1, 1, 10, 0)
  val endingTime = LocalDateTime.of(2023, 1, 1, 16, 30)
  val start2 = LocalDateTime.of(2023, 12, 31, 10, 0)
  val end2 = LocalDateTime.of(2023, 12, 31, 12, 15)

  "addEvent and deleteEvent" should "create and add an Event to the calendar, and remove the Event from list of all events" in {
    val myEvent = Event("chess", startingTime, endingTime,  "moro, shakki, aarni")
    subject.addEvent(myEvent)
    assert(subject.getAllEvents.size === 1)
    println(subject.getAllEvents.head)
    
    subject.deleteEvent(myEvent)
    assert(subject.getAllEvents.isEmpty)

  }
  val e1 = Event("yksi", startingTime, endingTime, "moro")
  val e2 = Event("kaksi", startingTime, endingTime, "moro")
  val e3 = Event("kolme", startingTime, endingTime, "yksi")
  val e4 = Event("neljä", startingTime, endingTime)

  "searchEvents" should "return correct list of events" in {
    subject.addEvent(e1)
    subject.addEvent(e2)
    subject.addEvent(e3)
    subject.addEvent(e4)

    println(subject.searchEvents("yksi"))
    println(subject.searchEvents("gaming"))
    println(subject.searchEvents("moro"))
    subject.deleteEvent(e1)
    println(subject.searchEvents("yksi"))
  }
  "Week" should "contain correct events" in {
    val cal2 = Calendar()
    cal2.addEvent(Event("koulu", startingTime, endingTime))
    cal2.addEvent(Event("työt", start2, end2))

    println(cal2.getCurrentWeek)
    println(cal2.getCurrentDay)
    println(cal2.getCurrentDay.getYear())
  }
  "week" should "be correctly moved forward" in {
    val cal2 = Calendar()
    println(cal2.getCurrentWeek)
    println(cal2.getCurrentDay)
    println(cal2.getCurrentDay.getYear())
    cal2.showNextWeek()
    println(cal2.getCurrentWeek)
    println(cal2.getCurrentDay)
    println(cal2.getCurrentDay.getYear())

  }
  "week and day" should "be correctly moved backward" in {
    val cal = Calendar()
    println("Initial week and day: " + cal.getCurrentWeek.toString + " " + cal.getCurrentDay.toString())
    cal.showPreviousWeek()
    println("Previous week and day: " + cal.getCurrentWeek.toString + " " + cal.getCurrentDay.toString() + "\n")
    // Edge case where previous week is in the last year
  }
  "Calendar" should "display correct events in current week" in{
    // First we initiate a calendar object
    val calendar = Calendar()
    // calendar should be initialized with currently ongoing week
    println(calendar.getCurrentWeek)
    // and event added to current week should show in all of calendars events
    // as well as in the week's events
    calendar.addEvent(Event("koulu", LocalDateTime.of(2023, 3, 4, 12, 0), LocalDateTime.of(2023, 3, 4, 13, 0), "fysiikka", "kandikeskus, luokka Y308"))
    calendar.addEvent(Event("duuni", LocalDateTime.of(2023, 3, 10, 12, 0), LocalDateTime.of(2023, 3, 11, 16, 0), "jutiland"))
    println(calendar.getAllEvents) 
    println(calendar.getCurrentWeek.getEvents)
    // calendar should change the week forward and show the next week's event in current events as well as all events
    calendar.showNextWeek()
    println(calendar.getCurrentWeek)
    println(calendar.getAllEvents) 
    println(calendar.getCurrentWeek.getEvents)
    calendar.showNextWeek()
    println(calendar.getCurrentWeek)
    println(calendar.getCurrentWeek.getEvents)
    // calendar should move backwards and show correct events
    calendar.showPreviousWeek()
    println(calendar.getCurrentWeek.getEvents)
    calendar.showPreviousWeek()
    println(calendar.getCurrentWeek.getEvents)
  }

end CalendarTest

class ServiceTest extends AnyFlatSpec, Matchers:
  val subject = GetWeek

  "nextOrdinalToLDT" should "return a correct new date when given an offset" in {
    val today = LocalDateTime.now()
    println(today)
    // standard case of shifting date in the middle of the year
    // first forwards:
    println(subject.nextOrdinalToLDT(today, 1))
    // then backwards:
    println(subject.nextOrdinalToLDT(today, -3))
    
    // FIXME: hopping years still broken
    // edge case where the year changes from December of X to January of X + 1
    val endOfYear = LocalDateTime.of(2022, 12, 29, 10, 0)
    println("should be 31st and 2nd")
    println(subject.nextOrdinalToLDT(endOfYear, 2))
    println(subject.nextOrdinalToLDT(endOfYear, 3))
    println(endOfYear.plusDays(3))

    // edge case where the year changes from January of X back to December of X - 1
    val startOfYear = LocalDateTime.of(2023, 1, 3, 10, 0)
    println(subject.nextOrdinalToLDT(startOfYear, -7))
  }

class LDTTest extends AnyFlatSpec, Matchers:
  val ldt1 = LocalDateTime.of(2023, 10, 10, 10, 10)
  val ldt2 = LocalDateTime.of(2023, 10, 10, 10, 11)
  "compareTo" should "return boolean between these LDTs" in {
    println(ldt1.equals(ldt2))
  }

