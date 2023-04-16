package gui_elements

import scalafx.scene._
import scalafx.Includes._
import scalafx.scene.control._
import scalafx.scene.text._
import scalafx.scene.image._
import scalafx.geometry._
import scalafx.scene.layout._
import scalafx.scene.paint._
import scalafx.stage._
import scalafx.event.ActionEvent
import gui_elements.MainGUI.primaryStage

val settings = new MenuItem("Settings")
val exit = new MenuItem("Exit") {
    onAction = () =>
        primaryStage.close()
}

val default = new MenuItem("Default")
val light = new MenuItem("Light")
val dark = new MenuItem("Dark")
val saatana = new MenuItem("saatana")


val menu1 = new Menu("Calendar") {
    items_=(List(settings, exit))
}
val menu2 = new Menu("Theme") {
    items_=(List(default, light, dark, saatana))
}

val menuBar = new MenuBar {
    menus_=(List(menu1, menu2))
}