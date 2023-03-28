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

val allDayLabels = Buffer[Label]()

var singleDayLabel = new Label

val sep1 = new Separator {
    prefWidth_=(rootWidth * 0.75)
    layoutY = 25
}

val bannerBoxWeek = new Pane {
    for c <- 0 to 6 do
        val date = calendar1.getCurrentWeek.getInterval.start.plusDays(c)
        children += new Pane {
                layoutX = c * 137
                children += new Label(s"${date.getDayOfWeek().toString.substring(0, 3)} ${date.getDayOfMonth()}.${date.getMonth().getValue()}") {
                            font_=(new Font(20))
                            alignment_=(Pos.Center)
                        }
                prefHeight_=(25)
                prefWidth_=(137)
                }
    prefHeight_=(50)
    children += sep1
    background = Background.fill(Color.LightGreen)
}

val oneDay = new VBox {
    prefHeight_=(625)
    prefWidth_=(rootWidth * 0.75)
    // children +=
    background_=(Background.fill(Color.AliceBlue))
}

val oneWeek = new Pane {
    prefHeight = 900
    prefWidth = rootWidth * 0.75
    for c <- 0 to 23 do
        children += new Label {
            if c < 10 then
                text = s"0$c:00"
            else
                text = s"$c:00"
            opacity = 0.5
            font = new Font(20)
            layoutY = c * 35
        }
    
    background_=(Background.fill(Color.LightCyan))

    onMouseClicked = (e:MouseEvent) => println(e.sceneX + " " + e.sceneY)
}

val scrollPaneDaily = new ScrollPane {
    content = oneDay
}
val scrollPaneWeekly = new ScrollPane {
    maxHeight = 580
    content = oneWeek
}