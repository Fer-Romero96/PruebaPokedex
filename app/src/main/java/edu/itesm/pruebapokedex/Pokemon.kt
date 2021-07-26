package edu.itesm.pruebapokedex

data class Pokemon (
    val forms : List<Forms>,
    val sprites : Sprites

)

data class Forms(
    val name : String
)
data class Sprites(

    val front_default: String

)