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
import gui_elements.MainGUI.weekEventPanes
import scalafx.stage._

val defFont = new Font(12)

val nameLabel = new Label("Name:") {
    layoutX = 20
    layoutY = 20
}
val nameTxtField = new TextField {
    promptText = "Event name"
    font = defFont
    prefWidth = 160
    layoutX = 130
    layoutY = 17
}
val extrainfoLabel = new Label("Additional info:") {
    layoutX = 20
    layoutY = 50
}
val extrainfoTxtField = new TextField {
    promptText = "Add info (optional)"
    font = defFont
    prefWidth = 160
    layoutX = 130
    layoutY = 47
}
