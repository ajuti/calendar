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
    prefHeight = 625
    prefWidth = rootWidth * 0.75
    children_=(List(bannerBoxWeek))
    for c <- 1 to 6 do
        children += new Separator {
            prefHeight = rootHeigth * 0.75
            layoutX = c * 137
            orientation_=(Orientation.Vertical)
        }
    background_=(Background.fill(Color.LightCyan))

    onMouseClicked = () =>
        
}

val scrollPaneDaily = new ScrollPane {
    content = oneDay
}
val scrollPaneWeekly = new ScrollPane {
    content = oneWeek
}