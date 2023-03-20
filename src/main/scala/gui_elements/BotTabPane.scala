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
import scalafx.scene.control.TabPane._

val weeklyTab = new Tab {
    text = "Weekly"
}
val dailyTab = new Tab {
    text = "Daily"
    content = scrollPaneDaily
}
val botPane = new TabPane {
    tabs = List(weeklyTab, dailyTab)
    tabClosingPolicy_=(TabClosingPolicy.Unavailable)
    maxHeight_=(500)
    prefWidth_=(rootWidth * 0.75 - 5)
    background = Background.fill(Color.BlanchedAlmond)
}