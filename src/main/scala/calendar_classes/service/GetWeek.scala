package calendar_classes.service
import java.time.{DayOfWeek, LocalDateTime}

object GetWeek:
  def getWeek(date: LocalDateTime) =
    var offSet: Int = 0
    date.getDayOfWeek match
      case DayOfWeek.MONDAY     =>
      case DayOfWeek.TUESDAY    => offSet = 1
      case DayOfWeek.WEDNESDAY  => offSet = 2
      case DayOfWeek.THURSDAY   => offSet = 3
      case DayOfWeek.FRIDAY     => offSet = 4
      case DayOfWeek.SATURDAY   => offSet = 5
      case DayOfWeek.SUNDAY     => offSet = 6
    end match

    1 + ((date.getDayOfYear - offSet - 1) / 7)
  end getWeek

  def getWeek2(date: LocalDateTime) =
    val ordinalDay = date.getDayOfYear

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

    def isLeapYear: Boolean =
      val year = date.getYear
      (year % 4 == 0) && (year % 100 != 0) || (year % 400 == 0)
    end isLeapYear

    if weekNum == 0 then
      52
    else if weekNum == 53 then
      if date.getDayOfWeek == DayOfWeek.FRIDAY || (date.getDayOfWeek == DayOfWeek.SATURDAY && isLeapYear) then
        53
      else
        1
    else
      weekNum
    end if
  end getWeek2

  def weeksInAYear(date: LocalDateTime) =
    
end GetWeek
