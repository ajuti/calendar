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
    
end GetWeek
