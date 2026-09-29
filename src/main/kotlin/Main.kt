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
            Path.of("datos", "datos_ini").toFile().mkdirs()
        }
        if(!rutaDatosFin.exists()){
            println("Creación de ruta para DATOS_FIN")
            Path.of("datos", "datos_fin").toFile().mkdirs()
        }
    } else {
        println("Prueba: el directorio ya existía")
    }


    val videojuegosCsv : Path = Path.of("datos", "videojuegos_csv")
    if(!videojuegosCsv.exists()){
        println("Creando videojuegos CSV")

    }

}