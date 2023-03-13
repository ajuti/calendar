package calendar_classes.service

import com.github.tototoshi.csv._
import java.io.File
import calendar_classes._
import scala.collection.mutable.Buffer
import java.{util => ju}
import java.io.FileNotFoundException
import java.io.IOException
import java.io.Writer
import calendar_classes.service._

class FileWriter(filePath: String, events: Buffer[Event]):

    def writeAllEvents() =
        try 
            val writer = CSVWriter.open(filePath)

            val parser = Parser()

            for c <- events do
                writer.writeRow(parser.eventToListOfStrings(c))
            end for

            writer.close()

        catch
            case e: FileNotFoundException => println("Destination file not found")
            case e: IOException => println("IOException when trying to write to the file")
        
    end writeAllEvents
end FileWriter