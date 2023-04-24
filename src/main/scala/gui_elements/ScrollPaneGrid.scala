package gui_elements

import scalafx.scene.layout.FlowPane
import scalafx.scene._
import scalafx.Includes._
import scalafx.scene.control._
import scalafx.scene.text._
import scalafx.scene.image._
import scalafx.geometry._
import scalafx.scene.layout._
import scalafx.scene.paint._
import gui_elements.MainGUI.calendar1
import scala.collection.mutable.Buffer
import scalafx.event._
import scalafx.scene.input.MouseEvent
import scalafx.scene.shape.Rectangle
import javafx.scene.input.MouseDragEvent
import scala.math._
import gui_elements.WindowGenerator.genNewPopupFromClick
import scalafx.scene.control.ScrollPane.ScrollBarPolicy
import gui_elements.MainGUI.popupOpen
import gui_elements.WindowGenerator
import java.time._

var clickToEdit = false

var eventToolTip = new Tooltip {
    id = "tooltip"
}

val allDayLabels = Buffer[Label]()

def getEasterDate = 
    val date = calculateEasterDate(calendar1.getCurrentWeek.getYearNum)
    LocalDateTime.of(calendar1.getCurrentWeek.getYearNum, date._2, date._1, 0, 0)
end getEasterDate

def getGoodFridayDate = getEasterDate.minusDays(2)

def getEasterMondayDate = getEasterDate.plusDays(1)

def getAscensionDate = getEasterDate.plusDays(39)

def getWhitSundayDate = getAscensionDate.plusDays(10)

def getMidsummerDate: LocalDateTime = 
    var midsummer = LocalDateTime.of(calendar1.getCurrentWeek.getYearNum, 6, 20, 0, 0)
    for i <- 1 to 6 do
        if midsummer.getDayOfWeek() != DayOfWeek.SATURDAY then
            midsummer = midsummer.plusDays(1)
    midsummer

def getAllhallowsDate = 
    var allhallows = LocalDateTime.of(calendar1.getCurrentWeek.getYearNum, 10, 31, 0, 0)
    for i <- 1 to 6 do
        if allhallows.getDayOfWeek() != DayOfWeek.SATURDAY then
            allhallows = allhallows.plusDays(1)
    allhallows

def holidayDates: Map[LocalDateTime, String] = 
    val year = calendar1.getCurrentWeek.getYearNum
    Map[LocalDateTime, String](
    LocalDateTime.of(year, 1, 1, 0, 0)      -> "New Years Day",
    LocalDateTime.of(year + 1, 1, 1, 0, 0)  -> "New Years Day",
    LocalDateTime.of(year - 1, 1, 1, 0, 0)  -> "New Years Day", //xdd, this is very bubblegum, will fix later (maybe)
    LocalDateTime.of(year, 12, 25, 0, 0)    -> "Christmas Day",
    getEasterDate                           -> "Easter Sunday",
    getEasterMondayDate                     -> "Easter Monday",
    getGoodFridayDate                       -> "Good Friday",
    getAscensionDate                        -> "Ascension Day",
    getWhitSundayDate                       -> "Whit Sunday",
    getMidsummerDate                        -> "Midsummer",
    getAllhallowsDate                       -> "Allhallows",
    LocalDateTime.of(year, 12, 26, 0, 0)    -> "Boxing Day",
    LocalDateTime.of(year, 1, 6, 0, 0)      -> "Epiphany",
    LocalDateTime.of(year, 5, 1, 0, 0)      -> "May Day",
    LocalDateTime.of(year, 12, 6, 0, 0)     -> "Independence Day",
    LocalDateTime.of(year, 12, 24, 0, 0)    -> "Christmas Eve",
    LocalDateTime.of(year, 2, 14, 0, 0)     -> "Valentines Day",
    LocalDateTime.of(year, 12, 31, 0, 0)    -> "New Years Eve"
)

var currentHolidayDates = holidayDates

// labels to be used in holiday pane
val holidayLabelsWeek = Buffer[Label]()

val holidayLabelsDay = Buffer[Label]()

val holidayPaneWeek = new Pane {
    children = holidayLabelsWeek
    layoutX = 47
    layoutY = 25
}
val holidayPaneDay = new Pane {
    children = holidayLabelsDay
    layoutX = 47
    layoutY = 25
}

def getHolidayLabels = 
    val dates = currentHolidayDates.keySet.filter(calendar1.getCurrentWeek.getInterval.contains(_))
    if dates.nonEmpty then
        for i <- dates do
            holidayLabelsWeek += new Label {
                text = currentHolidayDates.apply(i)
                layoutX = 8 + ((i.getDayOfWeek().getValue()- 1) * 130)
                layoutY = 4
                maxWidth = 130
                font = new Font(13)
            }
            if calendar1.getCurrentDate.toLocalDate().isEqual(i.toLocalDate()) then
                holidayLabelsDay += new Label {
                    text = currentHolidayDates.apply(i)
                    layoutX = 410
                    layoutY = 4
                    maxWidth = 130
                    font = new Font(13)
                }
end getHolidayLabels

def updateHolidayLabels(year: Int) = 
    if year != calendar1.getYearOfTheWeek then
        currentHolidayDates = holidayDates
    holidayLabelsWeek.clear()
    holidayLabelsDay.clear()
    getHolidayLabels
    holidayPaneWeek.children = holidayLabelsWeek
    holidayPaneDay.children = holidayLabelsDay
end updateHolidayLabels


val weekEvents = new Pane

val weekBannerEvents = new Pane {
    background = Background.fill(Color.White)
    prefWidth = rootWidth * 0.75 - 35
    minHeight = 34
}

val dayEvents = new Pane

val dayBannerEvents = new Pane {
    background = Background.fill(Color.White)
    prefWidth = rootWidth * 0.75 - 35
    minHeight = 34
}

var singleDayLabel = new Label {
    val day = calendar1.getCurrentDay.getLdt
    text = s"${day.getDayOfWeek().toString().toUpperCase()} ${day.getDayOfMonth()}.${day.getMonthValue()}."
    font = new Font(20)
    prefWidth = rootWidth * 0.75 - 45
    alignment = Pos.Center
}

def sep1 = new Separator {
    prefWidth_=(rootWidth * 0.75 - 47)
    layoutY = 25
    layoutX = 47
}
def sep2 = new Separator {
    prefWidth = rootWidth * 0.75 - 47
    layoutY = 50
    layoutX = 47
}
def showPop = 
    new Button("Add" + "\n" + "event") {
        layoutX = 2
        layoutY = 4
        prefWidth = 43
        prefHeight = 45
        onAction = () =>
            if !popupOpen then 
                val newPopup = WindowGenerator.genNewPopup()
                newPopup.show()
                popupOpen = true
    }

val scrollPaneWeekBanner = new ScrollPane {
    content = weekBannerEvents
    hbarPolicy = ScrollBarPolicy.Never
    background = Background.fill(Color.White)
    hmin = 1
    layoutX = 47
    layoutY = 50
    prefWidth = rootWidth * 0.75 - 35
    prefHeight = 30
    border = Border.stroke(Color.BLACK)
}
val scrollPaneDayBanner = new ScrollPane {
    content = dayBannerEvents
    hbarPolicy = ScrollBarPolicy.Never
    background = Background.fill(Color.White)
    hmin = 1
    layoutX = 47
    layoutY = 50
    prefWidth = rootWidth * 0.75 - 35
    prefHeight = 30
    border = Border.stroke(Color.BLACK)
}
val bannerBoxWeek = new Pane {
    for c <- 0 to 6 do
        val date = calendar1.getCurrentWeek.getInterval.start.plusDays(c)
        children += new Pane {
                layoutX = 45 + c * 130
                allDayLabels += new Label(s"${date.getDayOfWeek().toString.substring(0, 3)} ${date.getDayOfMonth()}.${date.getMonth().getValue()}") {
                            font_=(new Font(20))
                            prefWidth_=(130)
                            alignment_=(Pos.Center)
                        }
                children += allDayLabels(c)
                prefHeight_=(25)
                prefWidth_=(137)
                }
        children += new Separator {
            orientation_=(Orientation.Vertical)
            prefHeight_=(100)
            layoutX = 46 + c * 130
        }
    prefHeight_=(124)
    maxHeight = 150
    children += showPop
    children += sep1
    children += sep2
    children += scrollPaneWeekBanner
    children += holidayPaneWeek
    // background = Background.fill(Color.LightGreen)
}
val bannerBoxDay = new Pane {
    val date = calendar1.getCurrentDay.getLdt
    children += new Pane {
        layoutX = 45
        prefHeight_=(25)
        prefWidth_=(rootWidth * 0.75 - 45)
        children += singleDayLabel
    }
    prefHeight_=(124)
    maxHeight = 150
    children += showPop
    children += sep1
    children += sep2
    children += scrollPaneDayBanner
    children += holidayPaneDay
    // background = Background.fill(Color.LightGreen)
}

def gridView = new Pane { 
    for c <- 0 to 23 do
        children += new Label {
            if c < 10 then
                text = s"0$c:00"
            else
                text = s"$c:00"
            opacity = 0.5
            font = new Font(15)
            layoutY = 20 + c * 35
        }
        children += new Separator {
            prefWidth_=(rootWidth * 0.75 - 45)
            layoutY = 30 + c * 35
            layoutX = 38
            opacity_=(0.3)
        }
}
val oneDay = new Pane {
    prefHeight_=(870)
    prefWidth_=(rootWidth * 0.75)
    children += gridView
    children += dayEvents
    background_=(Background.fill(Color.White))

    onMouseClicked = (e:MouseEvent) => 
        if !popupOpen && !clickToEdit && e.y > 30 then
            val clickedPopup = WindowGenerator.genNewPopupFromClick(e.x, e.y, true)
            clickedPopup.show()
            popupOpen = true
}
val oneWeek = new Pane {
    prefHeight = 870
    prefWidth = rootWidth * 0.75
    children += gridView
    children += dayBannerEvents
    for c <- 0 to 6 do
        children += new Separator {
            orientation_=(Orientation.Vertical)
            prefHeight_=(870)
            layoutX = 45 + c * 130
        }
    children += weekEvents
    background_=(Background.fill(Color.White))

    onMouseClicked = (e:MouseEvent) => 
        println(e.x + " " + e.y)
        if !popupOpen && !clickToEdit && e.y > 30 then
            val clickedPopup = WindowGenerator.genNewPopupFromClick(e.x, e.y)
            clickedPopup.show()
            popupOpen = true
            
}

val scrollPaneDaily = new ScrollPane {
    maxHeight = 560
    content = oneDay
    prefWidth = rootWidth * 0.75 + 10
    vvalue = 0.6
    hbarPolicy = ScrollBarPolicy.Never
    hmin = 1
}
val scrollPaneWeekly = new ScrollPane {
    maxHeight = 560
    prefWidth = rootWidth * 0.75 + 10
    content = oneWeek
    vvalue_=(0.6)
    hbarPolicy = ScrollBarPolicy.Never
    hmin = 1
}