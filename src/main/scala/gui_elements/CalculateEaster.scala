package gui_elements

import scala.math._

// credits to assa.org.au
// their calculation just converted into Scala below

def calculateEasterDate(y: Int): (Int, Int) = {
  var firstDig, remain19, temp = 0
  var tA, tB, tC, tD, tE = 0

  firstDig = y / 100
  remain19 = y % 19

  temp = (firstDig - 15) / 2 + 202 - 11 * remain19

  firstDig match {
    case 21 | 24 | 25 | 27 | 28 | 29 | 30 | 31 | 32 | 34 | 35 | 38 =>
      temp = temp - 1
    case 33 | 36 | 37 | 39 | 40 =>
      temp = temp - 2
    case _ =>
  }
  temp = temp % 30

  tA = temp + 21
  if (temp == 29) tA = tA - 1
  if (temp == 28 && remain19 > 10) tA = tA - 1

  tB = (tA - 19) % 7

  tC = (40 - firstDig) % 4
  if (tC == 3) tC = tC + 1
  if (tC > 1) tC = tC + 1

  temp = y % 100
  tD = (temp + temp / 4) % 7

  tE = ((20 - tB - tC - tD) % 7) + 1
  val d = tA + tE

  if (d > 31) (d - 31, 4)
  else (d, 3)
}