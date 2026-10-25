import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.file.Files
import java.nio.file.Path

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

fun obtenerRutaIni(nombreFichero: String): Path {
    val rutaProyecto = Path.of("proyecto", "datos", "datos_ini", nombreFichero)
    return if (Files.exists(rutaProyecto)) {
        rutaProyecto
    } else {
        Path.of("datos_ini", nombreFichero)
    }
}

fun obtenerRutaFin(nombreFichero: String): Path {
    val dirProyecto = Path.of("proyecto", "datos", "datos_fin")
    return if (Files.isDirectory(dirProyecto)) {
        dirProyecto.resolve(nombreFichero)
    } else {
        Path.of("datos_fin", nombreFichero)
    }
}

fun leerCSV(ruta: Path): List<Videojuego> {
    var videojuegos: List<Videojuego> = emptyList()
    if (!Files.isReadable(ruta)) {
        println("Error: No se puede leer el fichero en la ruta: $ruta")
    } else {
        val reader = csvReader { delimiter = ';' }
        val filas: List<List<String>> = reader.readAll(ruta.toFile())
        videojuegos = filas.mapNotNull { columnas ->
            if (columnas.size >= 5) {
                try {
                    val id = columnas[0].toInt()
                    val titulo = columnas[1]
                    val genero = columnas[2]
                    val anio = columnas[3].toInt()
                    val nota = columnas[4].toDouble()
                    Videojuego(id, titulo, genero, anio, nota)
                } catch (e: Exception) {
                    println("Fila inválida ignorada: $columnas -> Error: ${e.message}")
                    null
                }
            } else {
                println("Fila con formato incompleto ignorada: $columnas")
                null
            }
        }
        println("--- Información leída con éxito de: $ruta")
    }
    return videojuegos
}

fun escribirCSV(ruta: Path, lista: List<Videojuego>) {
    try {
        val fichero: File = ruta.toFile()
        csvWriter { delimiter = ';' }.writeAll(
            lista.map { juego ->
                listOf(
                    juego.id.toString(),
                    juego.titulo,
                    juego.genero,
                    juego.anio.toString(),
                    juego.nota.toString()
                )
            },
            fichero
        )
        println("--- Información guardada con éxito en: $fichero")
    } catch (e: Exception) {
        println("Error al escribir el fichero CSV: ${e.message}")
    }
}

fun leerVideojuegosXML(ruta: Path): List<Videojuego> {
    var contenedor = VideojuegosXML(emptyList())
    if (!Files.isReadable(ruta)) {
        println("Error: No se puede leer el fichero en la ruta: $ruta")
    } else {
        try {
            val fichero = ruta.toFile()
            val xmlMapper = XmlMapper().registerKotlinModule()
            contenedor = xmlMapper.readValue(fichero)
            println("--- Información leída con éxito de: $ruta")
        } catch (e: Exception) {
            println("Error al leer XML: ${e.message}")
        }
    }
    return contenedor.listaVideojuegos
}

fun escribirVideojuegosXML(ruta: Path, lista: List<Videojuego>) {
    try {
        val fichero = ruta.toFile()
        val contenedor = VideojuegosXML(lista)
        val xmlMapper = XmlMapper().registerKotlinModule()
        val xmlString = xmlMapper.writerWithDefaultPrettyPrinter().writeValueAsString(contenedor)
        fichero.writeText(xmlString)
        println("--- Información guardada en XML: $fichero")
    } catch (e: Exception) {
        println("Error al guardar XML: ${e.message}")
    }
}

fun leerVideojuegosJSON(ruta: Path): List<Videojuego> {
    var videojuegos: List<Videojuego> = emptyList()
    if (!Files.isReadable(ruta)) {
        println("Error: No se puede leer el fichero en la ruta: $ruta")
    } else {
        try {
            val jsonString = Files.readString(ruta)
            videojuegos = Json.decodeFromString<List<Videojuego>>(jsonString)
            println("--- Información leída con éxito de: $ruta")
        } catch (e: Exception) {
            println("Error al leer JSON: ${e.message}")
        }
    }
    return videojuegos
}

fun escribirVideojuegosJSON(ruta: Path, lista: List<Videojuego>) {
    try {
        val jsonConfigurador = Json { prettyPrint = true }
        val jsonString = jsonConfigurador.encodeToString(lista)
        Files.writeString(ruta, jsonString)
        println("--- Información guardada en: $ruta")
    } catch (e: Exception) {
        println("Error al guardar JSON: ${e.message}")
    }
}

fun convertir(
    origen: Path,
    destino: Path,
    leer: (Path) -> List<Videojuego>,
    escribir: (Path, List<Videojuego>) -> Unit
) {
    if (!Files.exists(origen)) {
        println("Error: El fichero origen no existe en la ruta: $origen")
        return
    }

    try {
        val directorioDestino = destino.parent
        if (directorioDestino != null && !Files.exists(directorioDestino)) {
            Files.createDirectories(directorioDestino)
        }

        val lista = leer(origen)
        escribir(destino, lista)
        println("Conversión realizada con éxito.")
        println("Ruta del fichero generado: $destino")
        println("Número de registros convertidos: ${lista.size}")
    } catch (e: Exception) {
        println("Error al convertir el fichero: ${e.message}")
    }
}
