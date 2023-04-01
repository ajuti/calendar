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

val popupRootPane = new Pane {
    children = List(nameLabel, nameTxtField, extrainfoLabel, extrainfoTxtField)
}
val addEventPopup = new Stage {
    width_=(300)
    height_=(500)
    title = "Add new event"
    alwaysOnTop_=(true)
    scene = new Scene(300, 500) {
        
        root = popupRootPane
    }
}