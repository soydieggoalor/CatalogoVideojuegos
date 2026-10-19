import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import java.io.File
import kotlin.io.FileAlreadyExistsException

object VideojuegoRepository {

    private val archivo = File(
        "proyecto/datos/datos_ini/videojuegos.csv"
    )

    fun leer(): List<Videojuego> {
        return csvReader { delimiter = ';' }
            .readAll(archivo)
            .mapNotNull { fila ->
                if (fila.size != 5) {
                    null
                } else {
                    val id = fila[0].toIntOrNull()
                    val titulo = fila[1]
                    val genero = fila[2]
                    val anio = fila[3].toIntOrNull()
                    val nota = fila[4].toDoubleOrNull()

                    if (
                        id != null &&
                        titulo.isNotBlank() &&
                        genero.isNotBlank() &&
                        anio != null &&
                        nota != null &&
                        nota in 0.0..10.0
                    ) {
                        Videojuego(
                            id, titulo, genero, anio, nota
                        )
                    } else {
                        null
                    }
                }
            }
    }

    fun guardar(lista: List<Videojuego>) {
        csvWriter { delimiter = ';' }
            .writeAll(
                lista.map { videojuego ->
                    listOf(
                        videojuego.id.toString(),
                        videojuego.titulo,
                        videojuego.genero,
                        videojuego.anio.toString(),
                        videojuego.nota.toString()
                    )
                },
                archivo
            )
    }
}
