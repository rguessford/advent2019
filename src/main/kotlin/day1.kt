import java.io.File

fun main(args: Array<String>){
    File("src/main/resources/day1input.txt").forEachLine { x -> print(x) }
}