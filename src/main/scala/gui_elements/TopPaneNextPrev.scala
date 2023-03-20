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

val rootWidth = 1280
val rootHeigth = 720

val label1 = new Label {
        text = "JAN 2023 | WEEK  1"
        font = new Font(50)
}
val prevButton = new Button() {
        graphic_=(new ImageView(new Image("file:leftarrow.png")))
}
val nextButton = new Button {
    graphic_=(new ImageView(new Image("file:rightarrow.png")))
}
val topPane = new FlowPane(Orientation.Horizontal) {
    alignment_=(Pos.Center)
    prefWidth_=(rootWidth * 0.75 - 5)
    prefHeight_=(rootHeigth*0.10)
    children = List(prevButton, label1, nextButton)
    background = Background.fill(Color.DarkTurquoise)
}