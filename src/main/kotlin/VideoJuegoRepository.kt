import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.nio.file.Files
import java.nio.file.Path

object VideojuegoRepository {

    private val ruta: Path = Path.of("datos", "datos_ini", "videojuegos.csv")

    fun leer(): List<Videojuego> {
        if (!Files.isReadable(ruta)) {
            println("Aviso: no se puede leer $ruta. Se empieza con un catálogo vacío.")
            return emptyList()
        }

        val filas: List<List<String>> = csvReader { delimiter = ';' }.readAll(ruta.toFile())

        return filas.mapNotNull { columnas ->
            if (columnas.size >= 5) {
                try {
                    Videojuego(
                        id = columnas[0].toInt(),
                        titulo = columnas[1],
                        genero = columnas[2],
                        anio = columnas[3].toInt(),
                        nota = columnas[4].toDouble()
                    )
                } catch (e: Exception) {
                    println("Fila inválida ignorada: $columnas -> ${e.message}")
                    null
                }
            } else {
                println("Fila con formato incompleto ignorada: $columnas")
                null
            }
        }
    }

    fun guardar(lista: List<Videojuego>) {
        try {
            Files.createDirectories(ruta.parent)
            csvWriter { delimiter = ';' }.writeAll(
                lista.map {
                    listOf(it.id.toString(), it.titulo, it.genero, it.anio.toString(), it.nota.toString())
                },
                ruta.toFile()
            )
        } catch (e: Exception) {
            println("Error al escribir el fichero CSV: ${e.message}")
        }
    }
}