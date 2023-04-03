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
import gui_elements.MainGUI.weekEventPanes

val allDayLabels = Buffer[Label]()

val allHolidayLabels = Buffer[Label]()

var singleDayLabel = new Label

val sep1 = new Separator {
    prefWidth_=(rootWidth * 0.75 - 47)
    layoutY = 25
    layoutX = 47
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
            prefHeight_=(50)
            layoutX = 46 + c * 130
        }
    prefHeight_=(50)
    children += sep1
    // background = Background.fill(Color.LightGreen)
}

val oneDay = new VBox {
    prefHeight_=(625)
    prefWidth_=(rootWidth * 0.75)
    // children +=
    background_=(Background.fill(Color.AliceBlue))
}
val weekEvents = new Pane {
    for c <- weekEventPanes do
        children += c
}

val oneWeek = new Pane {
    prefHeight = 870
    prefWidth = rootWidth * 0.75
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
    for c <- 0 to 6 do
        children += new Separator {
            orientation_=(Orientation.Vertical)
            prefHeight_=(870)
            layoutX = 45 + c * 130
        }
    /*for c <- allEventPanes do
        children += c
    */
    children += weekEvents
    background_=(Background.fill(Color.White))

    onMouseClicked = (e:MouseEvent) => println(e.x + " " + e.y)
    //onMouseDragged = (e:MouseEvent) => println(e.x + " " + e.y)
    //onMouseDragOver = (e:MouseDragEvent) => println(e.x + " " + e.y)
        /*children += new Rectangle {
            prefHeight_=(abs(e.y - e.sceneY))
            prefWidth_=(abs(e.x - e.sceneX))
            fill_=(Color.Black)
            layoutX_=(e.sceneX)
            layoutY_=(e.sceneY)
        }*/
}

val scrollPaneDaily = new ScrollPane {
    maxHeight_=(560)
    content = oneDay
}
val scrollPaneWeekly = new ScrollPane {
    maxHeight = 560
    content = oneWeek
    vvalue_=(0.6)
}