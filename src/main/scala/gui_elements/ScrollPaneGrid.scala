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

val monday = new FlowPane {
    border = Border.stroke(Color.DarkMagenta)
    prefHeight_=(100)
}

val scrollPaneDaily = new ScrollPane {
    content = monday
    //monday.setLayoutX(50)
}