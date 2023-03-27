package gui_elements

import scalafx.scene._
import scalafx.Includes._
import scalafx.scene.control._
import scalafx.scene.text._
import scalafx.scene.image._
import scalafx.geometry._
import scalafx.scene.layout._
import scalafx.scene.paint._

val settings = new MenuItem("Settings")
val exit = new MenuItem("Exit")


val menu1 = new Menu("Calendar") {
    items_=(List(settings, exit))
}

val menuBar = new MenuBar {
    menus_=(List(menu1))
}