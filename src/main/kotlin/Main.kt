import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.exists

fun main() {
    println("CREACIÓN DE RUTAS PROYECTO")
    println(" Creando rutas...")

    val rutaDatos: Path = Path.of("proyecto", "datos")
    val rutaDatosIni: Path = rutaDatos.resolve("datos_ini")
    val rutaDatosFin: Path = rutaDatos.resolve("datos_fin")

    if (!rutaDatosIni.exists()) {
        println(" Creación de ruta para DATOS_INI")
        Files.createDirectories(rutaDatosIni)
    }

    if (!rutaDatosFin.exists()) {
        println(" Creación de ruta para DATOS_FIN")
        Files.createDirectories(rutaDatosFin)
    }

    val videojuegosCsv: Path = rutaDatosIni.resolve("videojuegos.csv")

    if (Files.exists(videojuegosCsv)) {
        println("El fichero videojuegos.csv existe")
    } else {
        println("El fichero videojuegos.csv no existe")
    }

    println("MOSTRANDO ESTRUCTURA DE DIRECTORIOS Y FICHEROS")
    println(" [DIR] ${rutaDatos.fileName}")

    mostrarruta(rutaDatos, 2)
}

fun mostrarruta(ruta: Path, nivel: Int) {
    Files.list(ruta).use { contenido ->
        contenido.sorted().forEach { elemento ->
            if (Files.isDirectory(elemento)) {
                println(" ".repeat(nivel) + "[DIR] ${elemento.fileName}")
                mostrarruta(elemento, nivel + 1)
            } else {
                println(" ".repeat(nivel) + "[FILE] ${elemento.fileName}")
            }
        }
    }
}