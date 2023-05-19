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
import java.time.format.DateTimeFormatter
import gui_elements.WindowGenerator._

var monthlyDate = calendar1.getCurrentDate
var currentMonth = monthlyDate.getMonth()
var currentYear = monthlyDate.getYear()
var currentMonthText = s"${currentMonth}\n${currentYear}"
var monthStart = LocalDate.of(currentYear, currentMonth.getValue(), 1)
var firstDayOfWeek = monthStart.getDayOfWeek.getValue
var daysInMonth = monthStart.lengthOfMonth()
var daysInPrevMonth = monthStart.minusMonths(1).lengthOfMonth()

def changeMonth(next: Boolean): Unit = 
    if next then 
        monthlyDate = monthlyDate.plusMonths(1)
    else
        monthlyDate = monthlyDate.minusMonths(1)
    updateMonth()
    monthlyLabel.text = currentMonthText
    paneOfMonthly.children = List(weekDayLabels, grid, nextMonth, prevMonth, currentMonthButton, monthlyLabel)
end changeMonth

def updateMonth() =
    currentMonth = monthlyDate.getMonth()
    currentYear = monthlyDate.getYear()
    currentMonthText = s"${currentMonth.toString().substring(0, 3)}\n${currentYear}"
    monthStart = LocalDate.of(currentYear, currentMonth.getValue(), 1)
    firstDayOfWeek = monthStart.getDayOfWeek.getValue
    daysInMonth = monthStart.lengthOfMonth()
    daysInPrevMonth = monthStart.minusMonths(1).lengthOfMonth()
end updateMonth

def getGrid = 
    new GridPane {
        hgap = 5
        vgap = 5
        padding = Insets(10)
        for (row <- 0 until 6; col <- 0 until 7) do
          val day = row * 7 + col + 2 - firstDayOfWeek
          val btn = 
            new Button {
                if (day > 0 && day <= daysInMonth) then
                    text = day.toString
                    if currentMonth == calendar1.getCurrentDate.getMonth() && day == calendar1.getCurrentDate.getDayOfMonth() then
                        style_=("-fx-border-color: black; -fx-border-radius: 3; -fx-background-radius: 3; -fx-background-color: aquamarine; -fx-font-size: 10;")
                    else
                        style_=("-fx-border-color: black; -fx-border-radius: 3; -fx-background-radius: 3; -fx-font-size: 10;")
                    onAction = (e: ActionEvent) =>
                        calendar1.setDayAndWeek(LocalDateTime.of(currentYear, currentMonth, day, LocalDateTime.now().getHour(), LocalDateTime.now().getMinute()))
                        updatePanes()
                        updateLabels()
                        updateHolidayLabels(currentYear)
                else 
                    if (day <= 0) then 
                        text = (daysInPrevMonth + day).toString()
                    else 
                        text = (day - daysInMonth).toString()
                    opacity = 0.75
                    style_=("-fx-border-color: black; -fx-border-radius: 3; -fx-background-radius: 3; -fx-font-size: 10;")
                    
                prefWidth = 32
            }

          add(btn, col, row)

        background = Background.fill(Color.DarkCyan)
        layoutY = 20
        border = Border.stroke(Color.Black)
    }

val nextMonth = new Button {
    text = "prev"
    onAction = () =>
        changeMonth(false)
    layoutX = rootWidth * 0.25 - 43
    layoutY = 50
}
val prevMonth = new Button {
    text = "next"
    onAction = () =>
        changeMonth(true)
    layoutY = 100
    layoutX = rootWidth * 0.25 - 43
}
val currentMonthButton: Button = new Button {
    text = "now"
    onAction = () =>
        monthlyDate = LocalDateTime.now()
        updateMonth()
        monthlyLabel.text = currentMonthText
        paneOfMonthly.children = List(weekDayLabels, grid, nextMonth, prevMonth, currentMonthButton, monthlyLabel)
    layoutY = 150
    layoutX = rootWidth * 0.25 - 43
}
val monthlyLabel = new Label {
    text = currentMonthText.substring(0, 3) + "\n" + currentYear
    font = new Font(15)
    layoutX = rootWidth * 0.25 - 40
    layoutY = 5
}

def grid = getGrid

val weekDayLabels = new Pane {
    var weekday = calendar1.getCurrentWeek.getInterval.start
    for i <- 0 to 6 do
        children += new Label{
            text = weekday.getDayOfWeek().toString().substring(0, 3)
            font = new Font(9)
            layoutX = i * 37.5 + 18
            layoutY = 5
        }
        weekday = weekday.plusDays(1)
    prefWidth = rootWidth * 0.25 - 47
    prefHeight = 20
    background = Background.fill(Color.DarkTurquoise)
}

val paneOfMonthly: Pane = new Pane{
    children = List(weekDayLabels, grid, nextMonth, prevMonth, currentMonthButton, monthlyLabel)
}