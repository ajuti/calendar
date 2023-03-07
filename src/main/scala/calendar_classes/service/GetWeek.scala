package calendar_classes.service
import java.time.{DayOfWeek, LocalDateTime}
import scala.math.*
import java.time.LocalDate
import java.time.Month

object GetWeek:

  def getWeek2(date: LocalDateTime) =
    val ordinalDay = date.getDayOfYear
    val ordinalYear = date.getYear

    def dayToInt(day: DayOfWeek) =
      day match
      case DayOfWeek.MONDAY     => 1
      case DayOfWeek.TUESDAY    => 2
      case DayOfWeek.WEDNESDAY  => 3
      case DayOfWeek.THURSDAY   => 4
      case DayOfWeek.FRIDAY     => 5
      case DayOfWeek.SATURDAY   => 6
      case DayOfWeek.SUNDAY     => 7
    end dayToInt

    val weekNum = (ordinalDay - dayToInt(date.getDayOfWeek) + 10) / 7

    if weekNum == 0 then
      52
    else if weekNum == 53 then
      if date.getDayOfWeek == DayOfWeek.THURSDAY || (date.getDayOfWeek == DayOfWeek.FRIDAY && this.isLeapYear(ordinalYear)) then
        53
      else
        1
    else
      weekNum
    end if
  end getWeek2
      
  def isLeapYear(yearNum: Int): Boolean =
    (yearNum % 4 == 0) && (yearNum % 100 != 0) || (yearNum % 400 == 0)
  end isLeapYear

  def weeksInAYear(date: LocalDateTime) = 
    val ordinalYear = date.getYear()
    def p(y: Int) = (y + (y / 4.0).floor - (y / 100.0).floor + (y / 400.0).floor) % 7

    if p(ordinalYear) == 4 || p(ordinalYear - 1) == 3 then 53 else 52
  end weeksInAYear

  def weeksInAYear(year: Int) = 
    def p(y: Int) = (y + (y / 4.0).floor - (y / 100.0).floor + (y / 400.0).floor) % 7

    if p(year) == 4 || p(year - 1) == 3 then 53 else 52
  end weeksInAYear



  // Edit: After figuring this method out for calculating new LocalDateTime object when shifting days,
  // I discovered that LocalDateTime has already has a method for it...
  // Just leaving this here to remind that maybe I should read the documentation properly next time :)

  // offSet may be negative, which means days are backtracked, positive means shifting forward
  def nextOrdinalToLDT(date: LocalDateTime, offSetIndex: Int): LocalDateTime = 
    val dayPlusOffset = date.getDayOfYear() + offSetIndex
    var dayNum = // new date's ordinal day number
      if this.isLeapYear(date.getYear()) then
        if dayPlusOffset >= 0 then
          if dayPlusOffset >= 367 then
            dayPlusOffset - 366
          else
            dayPlusOffset
        else
          366 + dayPlusOffset
      else
        if dayPlusOffset >= 0 then
          if dayPlusOffset >= 366 then
            dayPlusOffset - 365
          else
            dayPlusOffset
        else
          365 + dayPlusOffset

    val daysToMonth = Map[Range, Month](
      (1 to 31) -> Month.JANUARY,
      (32 to 59) -> Month.FEBRUARY,
      (60 to 90) -> Month.MARCH,
      (91 to 120) -> Month.APRIL,
      (121 to 151) -> Month.MAY,
      (152 to 181) -> Month.JUNE,
      (182 to 212) -> Month.JULY,
      (213 to 243) -> Month.AUGUST,
      (244 to 273) -> Month.SEPTEMBER,
      (274 to 304) -> Month.OCTOBER,
      (305 to 334) -> Month.NOVEMBER,
      (335 to 365) -> Month.DECEMBER
    )
    val daysToMonthInLeap = Map[Range, Month](
      (1 to 31) -> Month.JANUARY,
      (32 to 60) -> Month.FEBRUARY,
      (61 to 91) -> Month.MARCH,
      (92 to 121) -> Month.APRIL,
      (122 to 152) -> Month.MAY,
      (153 to 182) -> Month.JUNE,
      (183 to 213) -> Month.JULY,
      (214 to 244) -> Month.AUGUST,
      (245 to 274) -> Month.SEPTEMBER,
      (275 to 305) -> Month.OCTOBER,
      (306 to 335) -> Month.NOVEMBER,
      (336 to 366) -> Month.DECEMBER
    )
    var newDate = LocalDateTime.now()

    for c <- daysToMonth do
      if c._1.contains(dayNum)  then 
        if dayNum < date.getDayOfYear() && offSetIndex > 0 then
          newDate = LocalDateTime.of(date.getYear() + 1, c._2.getValue(), dayNum - c._1.head + 1, date.getHour(), date.getMinute())
        else if dayNum >= date.getDayOfYear() && offSetIndex < 0 then
          newDate = LocalDateTime.of(date.getYear() - 1, c._2.getValue(), dayNum - c._1.head + 1, date.getHour(), date.getMinute())
        else
          newDate = LocalDateTime.of(date.getYear(), c._2.getValue(), dayNum - c._1.head + 1, date.getHour(), date.getMinute())
    end for

    newDate
  
  end nextOrdinalToLDT


    
end GetWeek
