package utils

import java.awt.image.BufferedImage
import java.io.ByteArrayOutputStream
import javax.imageio.ImageIO

fun toByteArray(bitmap: BufferedImage) : ByteArray {
  val baos = ByteArrayOutputStream()
  ImageIO.write(bitmap, "png", baos)
  return baos.toByteArray()
}