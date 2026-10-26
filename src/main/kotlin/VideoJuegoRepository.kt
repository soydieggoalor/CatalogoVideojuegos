import com.github.doyaaaaaken.kotlincsv.dsl.csvReader
import com.github.doyaaaaaken.kotlincsv.dsl.csvWriter
import com.fasterxml.jackson.dataformat.xml.XmlMapper
import com.fasterxml.jackson.module.kotlin.readValue
import com.fasterxml.jackson.module.kotlin.registerKotlinModule
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.io.File
import java.nio.ByteBuffer
import java.nio.channels.FileChannel
import java.nio.file.Files
import java.nio.file.Path
import java.nio.file.StandardCopyOption
import java.nio.file.StandardOpenOption

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

const val TAMANO_ID = Int.SIZE_BYTES
const val TAMANO_TITULO = 40
const val TAMANO_GENERO = 20
const val TAMANO_ANIO = Int.SIZE_BYTES
const val TAMANO_NOTA = Double.SIZE_BYTES
const val TAMANO_REGISTRO = TAMANO_ID + TAMANO_TITULO + TAMANO_GENERO + TAMANO_ANIO + TAMANO_NOTA

fun obtenerRutaBin(): Path {
    val dirFin = Path.of("proyecto", "datos", "datos_fin")
    return if (Files.isDirectory(dirFin)) {
        dirFin.resolve("videojuegos.bin")
    } else if (Files.isDirectory(Path.of("datos_fin"))) {
        Path.of("datos_fin", "videojuegos.bin")
    } else {
        Path.of("videojuegos.bin")
    }
}

fun vaciarCrearFicheroBin(rutaBin: Path) {
    try {
        val parent = rutaBin.parent
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent)
        }
        FileChannel.open(
            rutaBin,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.TRUNCATE_EXISTING
        ).close()
        println("--- El fichero binario '${rutaBin.fileName}' se ha creado y está vacío.")
    } catch (e: Exception) {
        println("Error al vaciar o crear el fichero binario: ${e.message}")
    }
}

fun importarABinario(rutaBin: Path): Int {
    val rutaCSV = obtenerRutaIni("videojuegos.csv")
    if (!Files.exists(rutaCSV)) {
        println("No se encontró el fichero de origen en: $rutaCSV")
        return 0
    }

    val lista = leerCSV(rutaCSV)
    if (lista.isEmpty()) {
        println("El fichero origen está vacío o no se pudieron leer videojuegos.")
        return 0
    }

    vaciarCrearFicheroBin(rutaBin)
    for (juego in lista) {
        anadirVideojuegoBin(rutaBin, juego)
    }
    println("--- Información importada con éxito al fichero binario: ${lista.size} registros.")
    return lista.size
}

fun leerVideojuegosBin(rutaBin: Path): List<Videojuego> {
    val videojuegos = mutableListOf<Videojuego>()
    if (!Files.exists(rutaBin) || !Files.isReadable(rutaBin)) {
        return emptyList()
    }

    try {
        FileChannel.open(rutaBin, StandardOpenOption.READ).use { canal ->
            val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
            while (canal.read(buffer) > 0) {
                buffer.flip()
                val id = buffer.getInt()

                val tituloBytes = ByteArray(TAMANO_TITULO)
                buffer.get(tituloBytes)
                val titulo = String(tituloBytes, Charsets.ISO_8859_1).trim()

                val generoBytes = ByteArray(TAMANO_GENERO)
                buffer.get(generoBytes)
                val genero = String(generoBytes, Charsets.ISO_8859_1).trim()

                val anio = buffer.getInt()
                val nota = buffer.getDouble()

                videojuegos.add(Videojuego(id, titulo, genero, anio, nota))
                buffer.clear()
            }
        }
    } catch (e: Exception) {
        println("Error al leer el fichero binario: ${e.message}")
    }
    return videojuegos
}

fun obtenerVideojuegoPorIdBin(rutaBin: Path, idBuscado: Int): Videojuego? {
    if (!Files.exists(rutaBin) || !Files.isReadable(rutaBin)) return null

    try {
        FileChannel.open(rutaBin, StandardOpenOption.READ).use { canal ->
            val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
            while (canal.read(buffer) > 0) {
                buffer.flip()
                val id = buffer.getInt()

                val tituloBytes = ByteArray(TAMANO_TITULO)
                buffer.get(tituloBytes)
                val titulo = String(tituloBytes, Charsets.ISO_8859_1).trim()

                val generoBytes = ByteArray(TAMANO_GENERO)
                buffer.get(generoBytes)
                val genero = String(generoBytes, Charsets.ISO_8859_1).trim()

                val anio = buffer.getInt()
                val nota = buffer.getDouble()

                if (id == idBuscado) {
                    return Videojuego(id, titulo, genero, anio, nota)
                }
                buffer.clear()
            }
        }
    } catch (e: Exception) {
        println("Error al buscar en el fichero binario: ${e.message}")
    }
    return null
}

fun existeIdEnBinario(rutaBin: Path, id: Int): Boolean {
    return obtenerVideojuegoPorIdBin(rutaBin, id) != null
}

fun anadirVideojuegoBin(rutaBin: Path, juego: Videojuego) {
    try {
        val parent = rutaBin.parent
        if (parent != null && !Files.exists(parent)) {
            Files.createDirectories(parent)
        }

        FileChannel.open(
            rutaBin,
            StandardOpenOption.WRITE,
            StandardOpenOption.CREATE,
            StandardOpenOption.APPEND
        ).use { canal ->
            val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)

            buffer.putInt(juego.id)

            val tituloBytes = juego.titulo
                .take(TAMANO_TITULO)
                .padEnd(TAMANO_TITULO, ' ')
                .toByteArray(Charsets.ISO_8859_1)
            buffer.put(tituloBytes, 0, TAMANO_TITULO)

            val generoBytes = juego.genero
                .take(TAMANO_GENERO)
                .padEnd(TAMANO_GENERO, ' ')
                .toByteArray(Charsets.ISO_8859_1)
            buffer.put(generoBytes, 0, TAMANO_GENERO)

            buffer.putInt(juego.anio)
            buffer.putDouble(juego.nota)

            buffer.flip()
            while (buffer.hasRemaining()) {
                canal.write(buffer)
            }
        }
    } catch (e: Exception) {
        println("Error al añadir al fichero binario: ${e.message}")
    }
}

fun modificarNotaBin(rutaBin: Path, idVideojuego: Int, nuevaNota: Double): Boolean {
    if (!Files.exists(rutaBin)) {
        println("El fichero binario no existe.")
        return false
    }

    try {
        FileChannel.open(
            rutaBin,
            StandardOpenOption.READ,
            StandardOpenOption.WRITE
        ).use { canal ->
            val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
            var encontrado = false

            while (canal.read(buffer) > 0 && !encontrado) {
                val posicionActual = canal.position()
                buffer.flip()
                val id = buffer.getInt()

                if (id == idVideojuego) {
                    encontrado = true

                    val inicioRegistro = posicionActual - TAMANO_REGISTRO
                    val desplazamientoNota = TAMANO_ID + TAMANO_TITULO + TAMANO_GENERO + TAMANO_ANIO
                    val posicionNota = inicioRegistro + desplazamientoNota

                    canal.position(posicionNota)
                    val bufferNota = ByteBuffer.allocate(TAMANO_NOTA)
                    bufferNota.putDouble(nuevaNota)
                    bufferNota.flip()

                    while (bufferNota.hasRemaining()) {
                        canal.write(bufferNota)
                    }
                }
                buffer.clear()
            }
            return encontrado
        }
    } catch (e: Exception) {
        println("Error al modificar en el fichero binario: ${e.message}")
        return false
    }
}

fun eliminarVideojuegoBin(rutaBin: Path, idVideojuego: Int): Boolean {
    if (!Files.exists(rutaBin)) {
        println("El fichero binario no existe.")
        return false
    }

    val pathTemporal = Path.of(rutaBin.toString() + ".tmp")
    var encontrado = false

    try {
        FileChannel.open(rutaBin, StandardOpenOption.READ).use { canalLectura ->
            FileChannel.open(
                pathTemporal,
                StandardOpenOption.WRITE,
                StandardOpenOption.CREATE,
                StandardOpenOption.TRUNCATE_EXISTING
            ).use { canalEscritura ->
                val buffer = ByteBuffer.allocate(TAMANO_REGISTRO)
                while (canalLectura.read(buffer) > 0) {
                    buffer.flip()
                    val id = buffer.getInt()
                    if (id == idVideojuego) {
                        encontrado = true
                    } else {
                        buffer.rewind()
                        canalEscritura.write(buffer)
                    }
                    buffer.clear()
                }
            }
        }

        if (encontrado) {
            Files.move(pathTemporal, rutaBin, StandardCopyOption.REPLACE_EXISTING)
            return true
        } else {
            Files.deleteIfExists(pathTemporal)
            return false
        }
    } catch (e: Exception) {
        println("Error durante la eliminación en el fichero binario: ${e.message}")
        Files.deleteIfExists(pathTemporal)
        return false
    }
}

