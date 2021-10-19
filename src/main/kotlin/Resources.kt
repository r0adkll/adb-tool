object Resources {
  val androidWhite by stream("baseline_android_white_24dp.png")

  private fun stream(name: String) = lazy {
    Resources::class.java.getResourceAsStream(name)!!
  }
}
