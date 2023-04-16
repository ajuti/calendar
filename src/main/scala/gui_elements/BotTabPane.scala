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

val bannerPlusViewWeekly = new VBox {
    children = List(bannerBoxWeek, scrollPaneWeekly)
    prefWidth = rootWidth * 0.75 + 12
}
val bannerPlusViewDaily = new VBox {
    children = List(bannerBoxDay, scrollPaneDaily)
    prefWidth = rootWidth * 0.75 + 12
}
val weeklyTab = new Tab {
    text = "Weekly"
    content = bannerPlusViewWeekly
}
val dailyTab = new Tab {
    text = "Daily"
    content = bannerPlusViewDaily
}
val botPane = new TabPane {
    tabs = List(weeklyTab, dailyTab)
    tabClosingPolicy_=(TabClosingPolicy.Unavailable)
    prefHeight_=(850)
    
}