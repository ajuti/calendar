import org.scalatest.matchers.should.Matchers
import org.scalatest.flatspec.AnyFlatSpec
import calendar_classes._
import calendar_classes.service._
import java.time.*
import scalafx.scene.paint.Color
import scala.collection.mutable.Buffer
import java.util.regex.Matcher
import com.github.tototoshi.csv._
import gui_elements.calculateEasterDate

class EventTest extends AnyFlatSpec, Matchers:
  val startingTime = LocalDateTime.of(2023, 3, 27, 10, 0)
  val endingTime = LocalDateTime.of(2023, 1, 1, 16, 30)
  val subject = Event("work", Interval(startingTime, endingTime))

  "setColor" should "assign a ColorTag to an event" in {
    subject.setColor(10,10,10)
    assert(subject.getColor.nonEmpty)
    println(subject.getColor)
    // subject.setColor("#ab34de")
    println(subject.getColor)
    val b = Color.GRAY
    println(b.getRed() + " " + b.getGreen() + " " + b.getBlue())
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
end EventTest


class CalendarTest extends AnyFlatSpec, Matchers:
  "correct events" should "show up in corresponding weeks" in {
    val calendar = new Calendar
    val e1 = Event("yksi", Interval(LocalDateTime.now(), LocalDateTime.now().plusHours(3)))

    calendar.addEvent(e1)
    println(calendar.getCurrentDate)
    println(e1.getInterval.start)
    
    assert(e1.getInterval.intersects(calendar.getCurrentWeek.getInterval))

    println(calendar.getAllEvents)
    println(calendar.getCurrentWeek.getEvents)
  }
  "calendar" should "instatiate with correct events" in {
    val calendar = new Calendar
    println(calendar.getAllEvents)
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
  val ldt2 = LocalDateTime.of(2023, 10, 15, 10, 10)
  val testDay1 = LocalDateTime.of(2023, 10, 12, 10, 10)
  "compareTo" should "return boolean between these LDTs" in {
    println(ldt1.equals(ldt2))
  }

class IntervalTest extends AnyFlatSpec, Matchers:
  "intersect" should "return true if two intervals either contain or overlap each other" in {
    val interval = Interval(LocalDateTime.of(2023, 1, 3, 0, 0), LocalDateTime.of(2023, 1, 7, 23, 59))
    // Whole event is inside the interval | true
    val event1 = Interval(LocalDateTime.of(2023, 1, 5, 10, 0), LocalDateTime.of(2023, 1, 6, 10, 0))
    // The event is on the first day of the interval | true
    val event2 = Interval(LocalDateTime.of(2023, 1, 3, 10, 0), LocalDateTime.of(2023, 1, 3, 12, 0))
    // The event is on the last day of the interval | true
    val event3 = Interval(LocalDateTime.of(2023, 1, 7, 10, 0), LocalDateTime.of(2023, 1, 7, 15, 0))
    // Event is outside of the interval | false
    val event4 = Interval(LocalDateTime.of(2023, 1, 1, 10, 0), LocalDateTime.of(2023, 1, 2, 10, 0))
    // Event start is in but end is outside of the interval | true
    val event5 = Interval(LocalDateTime.of(2023, 1, 5, 10, 0), LocalDateTime.of(2023, 1, 9, 10, 0))
    // Event start is out but end is outside of the interval | true
    val event6 = Interval(LocalDateTime.of(2023, 1, 1, 10, 0), LocalDateTime.of(2023, 1, 4, 10, 0))
    // Event starts and ends outside the interval | true
    val event7 = Interval(LocalDateTime.of(2023, 1, 5, 10, 0), LocalDateTime.of(2023, 1, 16, 10, 0))

    println(interval.intersects(event1))
    println(interval.intersects(event2))
    println(interval.intersects(event3))
    println(interval.intersects(event4))
    println(interval.intersects(event5))
    println(interval.intersects(event6))
    println(interval.intersects(event7))
  }

  "length" should "tell correct length of an interval" in {
    val interval = Interval(LocalDateTime.now(), LocalDateTime.now().plusHours(6))
    println(interval.lengthInHours)
    println(interval.lengthInDays)
    println(interval.lengthInMinutes)
  }


end IntervalTest

class ParserTest extends AnyFlatSpec, Matchers:
  "toStringForm" should "return string in a wanted format" in {
    val parser = Parser()
    val events = Buffer[Event](Event("work", Interval(LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(1)), extraInfo = "moro"), Event("yksi", Interval(LocalDateTime.now(), LocalDateTime.now().plusHours(3)), bannerColor = Some(Color.BLACK)))
    println(parser.eventToListOfStrings(events.head))
  }
  "toEventFromString" should "create a new event with given string" in {
    val oneLine = "nimi,2023-03-08T12:00:00;2023-03-08T15:00:00,test,sup,125-100-80"
    val parser = Parser()

    println(parser.toEventFromString(oneLine).getColor)
  }
  
class ReaderTest extends AnyFlatSpec, Matchers:
  "filereader" should "correctly read csv-file and construct events" in {
    val fileIn = FileReader("events.csv")
    fileIn.addAllEvents()
    println(fileIn.events)
  }

class WriterTest extends AnyFlatSpec, Matchers:
  "filewriter" should "correctly write event-information to csv-file" in {
    // val events = Buffer[Event](Event("work", Interval(LocalDateTime.now().minusHours(2), LocalDateTime.now().plusHours(1)), extraInfo = "moro"), Event("yksi", Interval(LocalDateTime.now(), LocalDateTime.now().plusHours(3)), bannerColor = Some(Color.BLACK)))
    // val fileOut = FileWriter("events.csv", events)
    val calendar = Calendar()
    val events = Buffer[Event](Event("xdd", Interval(LocalDateTime.now().minusHours(2).withMinute(0).withSecond(0), LocalDateTime.now().plusHours(1).withMinute(0).withSecond(0)), extraInfo = "moro"), Event("aintnoway", Interval(LocalDateTime.now().withMinute(0).withSecond(0), LocalDateTime.now().plusDays(2).plusHours(2).withMinute(0).withSecond(0)), bannerColor = Some(Color.BLACK)))
    events.foreach(calendar.addEvent(_))
    calendar.upload()
  }

class EasterTest extends AnyFlatSpec, Matchers:
  "method" should "return correct date for easter when given a year number as parameter" in {
    val years = List(2015, 2016, 2017, 2018, 2019, 2020, 2021, 2022, 2023, 2024, 2025)

    years.foreach(x => println(calculateEasterDate(x)))
  }