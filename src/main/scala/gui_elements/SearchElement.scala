package gui_elements

import scalafx.scene._
import scalafx.Includes._
import scalafx.scene.paint._
import scalafx.geometry.Insets
import scalafx.scene.effect._
import scalafx.scene.control._
import scalafx.collections.ObservableBuffer
import scalafx.scene.layout._
import scalafx.geometry._
import scalafx.scene.text._
import scalafx.scene.image._
import scalafx.scene.control.TabPane._
import calendar_classes.Calendar
import scala.collection.mutable.Buffer
import scalafx.event.ActionEvent
import scalafx.scene.control.ScrollPane.ScrollBarPolicy
import scalafx.scene.input.KeyEvent
import scalafx.scene.input.KeyCode
import java.time.format.DateTimeFormatter

def searchEventPanes(input: String) = 
    val foundEvents = MainGUI.calendar1.searchEvents(input)
    accordion.panes.clear()
    for i <- foundEvents do
        accordion.panes += new TitledPane {
            text = i.getName
            content = new Pane {
                children += new Label {
                    text = i.getName + "\n" + i.getInterval.start.format(DateTimeFormatter.ISO_LOCAL_TIME).toString() + " - " + i.getInterval.end.format(DateTimeFormatter.ISO_LOCAL_TIME).toString
                    layoutX = 2
                    layoutY = 2
                    font = new Font(13)
                }
                prefWidth = rootWidth * 0.24
                prefHeight = 75
                background = Background.fill(i.getColor.getOrElse(Color.BlanchedAlmond))
                // ONCLICK
            }
        }

end searchEventPanes


val searchField = new TextField {
    prefWidth = rootWidth * 0.24
    promptText = "Search events"
    layoutX = 5
    layoutY = 5
    onKeyPressed = (ke: KeyEvent) =>
        if ke.getCode().getName() == "Enter" then
            searchEventPanes(this.text.getValue())
}

val accordion = new Accordion

val scrollAccordion = new ScrollPane {
    content = accordion
    prefHeight = 250
    prefWidth = rootWidth * 0.24
    layoutX = 5
    layoutY = 35
    hbarPolicy = ScrollBarPolicy.Never
    vbarPolicy = ScrollBarPolicy.AsNeeded
    hmin = 1
}
val searchRootPane = new Pane {
    children = List(searchField, scrollAccordion)
    prefHeight = 290
    background = Background.fill(Color.DarkCyan)
}