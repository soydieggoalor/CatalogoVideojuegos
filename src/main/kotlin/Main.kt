import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.Paths
import kotlin.io.path.Path
import kotlin.io.path.exists

fun main(){
    val rutaDatosIni: Path = Path.of("datos", "datos_ini") // /datos/
    val rutaDatosFin: Path = Path.of("datos", "datos_fin")


    if(!rutaDatosIni.exists() || !rutaDatosFin.exists()){
        println("Creando rutas...")
        if(!rutaDatosIni.exists()){
            println("Creación de ruta para DATOS_INI")
            Files.createDirectories(Path.of("datos", "datos_ini"))
        }
        if(!rutaDatosFin.exists()){
            println("Creación de ruta para DATOS_FIN")
            Files.createDirectories(Path.of("datos", "datos_fin"))
        }
    } else {
        println("Prueba: el directorio ya existía")
    }


    val videojuegosCsv : Path = Path.of("datos", "datos_ini", "videojuegos.csv")
    if(!videojuegosCsv.exists()){
        println("Creando videojuegos CSV")
        Files.createFile(Path.of("datos", "datos_ini", "videojuegos.csv"))
        if (videojuegosCsv.exists()){
            println("Archivo creado con éxito")
        } else {
            println("No se ha podido crear videojuegos CSV")
        }
    } else {
        println("El archivo Videojuegos CSV ya existía.")
    }

}