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

def updateLabels() =
        label1.text = s"${calendar1.getCurrentDate.getMonth().toString().substring(0, 3)} ${calendar1.getCurrentDate.getYear()} | ${calendar1.getCurrentWeek.toString.toUpperCase()}"
        for c <- 0 to 6 do
            val date = calendar1.getCurrentWeek.getInterval.start.plusDays(c)
            allDayLabels(c).text = s"${date.getDayOfWeek().toString.substring(0, 3)} ${date.getDayOfMonth()}.${date.getMonth().getValue()}"
        val day = calendar1.getCurrentDay.getLdt
        singleDayLabel.text = s"${day.getDayOfWeek().toString().toUpperCase()} ${day.getDayOfMonth()}.${day.getMonthValue()}."
end updateLabels

val rootHeigth = 720
val rootWidth = 1280

val label1 = new Label {
    text = s"${calendar1.getCurrentDate.getMonth().toString().substring(0, 3)} ${calendar1.getCurrentDate.getYear()} | ${calendar1.getCurrentWeek.toString.toUpperCase()}"
    font = new Font(50)
    prefWidth_=(rootWidth * 0.4)
    alignment_=(Pos.Center)
}
val prevButton = new Button() {
    graphic_=(new ImageView(new Image("file:leftarrow.png")))

    onAction = () =>
        if weeklyTab.isSelected() then
            calendar1.showPreviousWeek()

        if dailyTab.isSelected() then
            calendar1.showPreviousDay()

        weekEvents.children.clear()
        weekEvents.children_=(CreateEventPane.initializeWeek(calendar1.getCurrentWeek.getEvents))
        dayEvents.children.clear()
        dayEvents.children = CreateEventPane.initializeDay(calendar1.getCurrentDay.getEvents)

        updateLabels()
}
val nextButton = new Button {
    graphic_=(new ImageView(new Image("file:rightarrow.png")))

    onAction = () =>
        if weeklyTab.isSelected() then
            calendar1.showNextWeek()

        if dailyTab.isSelected() then
            calendar1.showNextDay()
        
        weekEvents.children.clear()
        weekEvents.children_=(CreateEventPane.initializeWeek(calendar1.getCurrentWeek.getEvents))
        dayEvents.children.clear()
        dayEvents.children = CreateEventPane.initializeDay(calendar1.getCurrentDay.getEvents)

        updateLabels()
}
val topPane = new HBox {
    alignment_=(Pos.Center)
    prefWidth_=(rootWidth * 0.75)
    // prefHeight_=(rootHeigth * 0.1)
    children = List(prevButton, label1, nextButton)
    background = Background.fill(Color.DarkTurquoise)
}