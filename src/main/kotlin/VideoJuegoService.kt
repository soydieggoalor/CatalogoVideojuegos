
fun listarVideojuegos(lista: List<Videojuego>) {
    if (lista.isEmpty()) {
        println("No hay videojuegos en el catálogo.")
        return
    }

    lista.forEach { videojuego ->
        println(
            "ID: ${videojuego.id} | " +
                    "Título: ${videojuego.titulo} | " +
                    "Género: ${videojuego.genero} | " +
                    "Año: ${videojuego.anio} | " +
                    "Nota: ${videojuego.nota}"
        )
    }
}

fun buscarPorId(lista: List<Videojuego>, id: Int) {
    val videojuego = lista.find { it.id == id }

    if (videojuego != null) {
        println(videojuego)
    } else {
        println("No existe ningún videojuego con ese ID.")
    }
}

fun anyadirVideojuego(lista: List<Videojuego>): List<Videojuego> {
    println("Introduce el título:")
    val titulo = readlnOrNull()?.trim()

    if (titulo.isNullOrBlank()) {
        println("El título no puede estar vacío.")
        return lista
    }

    println("Introduce el género:")
    val genero = readlnOrNull()?.trim()

    if (genero.isNullOrBlank()) {
        println("El género no puede estar vacío.")
        return lista
    }

    println("Introduce el año:")
    val anio = readlnOrNull()?.toIntOrNull()

    if (anio == null || anio <= 0) {
        println("Año no válido.")
        return lista
    }

    println("Introduce la nota (0-10):")
    val nota = readlnOrNull()?.toDoubleOrNull()

    if (nota == null || nota !in 0.0..10.0) {
        println("Nota no válida.")
        return lista
    }

    val nuevoId = (lista.maxOfOrNull { it.id } ?: 0) + 1

    val nuevoVideojuego = Videojuego(
        nuevoId, titulo, genero, anio, nota
    )

    println("Videojuego añadido correctamente.")
    return lista + nuevoVideojuego
}

fun modificarNota(
    lista: List<Videojuego>,
    id: Int,
    nuevaNota: Double
): List<Videojuego> {
    if (lista.none { it.id == id }) {
        println("No existe ningún videojuego con ese ID.")
        return lista
    }

    if (nuevaNota !in 0.0..10.0) {
        println("La nota debe estar entre 0 y 10.")
        return lista
    }

    println("Nota modificada correctamente.")

    return lista.map { videojuego ->
        if (videojuego.id == id) {
            videojuego.copy(nota = nuevaNota)
        } else {
            videojuego
        }
    }
}

fun eliminarVideojuego(
    lista: List<Videojuego>,
    id: Int
): List<Videojuego> {
    if (lista.none { it.id == id }) {
        println("No existe ningún videojuego con ese ID.")
        return lista
    }

    println("¿Seguro que quieres eliminarlo? (s/n)")
    val confirmacion = readlnOrNull()?.trim()?.lowercase()

    if (confirmacion == "s") {
        println("Videojuego eliminado.")
        return lista.filter { it.id != id }
    }

    println("Eliminación cancelada.")
    return lista
}

fun menuGestionCSV(catalogoActual: List<Videojuego>): List<Videojuego> {
    var videojuegos = catalogoActual
    var volver = false

    while (!volver) {
        println()
        println("--- Gestión CSV ---")
        println("1. Listar todos los videojuegos")
        println("2. Buscar videojuego por ID")
        println("3. Añadir nuevo videojuego")
        println("4. Modificar nota de un videojuego")
        println("5. Eliminar videojuego")
        println("0. Volver al menú principal")
        print("Elige una opción: ")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> listarVideojuegos(videojuegos)

            2 -> {
                print("Introduce el ID: ")
                val id = readlnOrNull()?.toIntOrNull()

                if (id == null) {
                    println("ID no válido.")
                } else {
                    buscarPorId(videojuegos, id)
                }
            }

            3 -> {
                videojuegos = anyadirVideojuego(videojuegos)
                VideojuegoRepository.guardar(videojuegos)
            }

            4 -> {
                print("Introduce el ID: ")
                val id = readlnOrNull()?.toIntOrNull()

                if (id == null) {
                    println("ID no válido.")
                } else if (videojuegos.none { it.id == id }) {
                    println("No existe ningún videojuego con ese ID.")
                } else {
                    print("Introduce la nueva nota (0-10): ")
                    val nota = readlnOrNull()?.toDoubleOrNull()

                    if (nota == null || nota !in 0.0..10.0) {
                        println("Nota no válida.")
                    } else {
                        videojuegos = modificarNota(videojuegos, id, nota)
                        VideojuegoRepository.guardar(videojuegos)
                    }
                }
            }

            5 -> {
                print("Introduce el ID: ")
                val id = readlnOrNull()?.toIntOrNull()

                if (id == null) {
                    println("ID no válido.")
                } else if (videojuegos.none { it.id == id }) {
                    println("No existe ningún videojuego con ese ID.")
                } else {
                    videojuegos = eliminarVideojuego(videojuegos, id)
                    VideojuegoRepository.guardar(videojuegos)
                }
            }

            0 -> volver = true

            null -> println("Introduce una opción numérica válida.")

            else -> println("Opción no válida.")
        }
    }

    return videojuegos
}

fun integrarCatalogo(
    catalogoActual: List<Videojuego>,
    nuevos: List<Videojuego>
): List<Videojuego> {
    val resultado = catalogoActual.toMutableList()
    var contadorNuevos = 0

    for (juego in nuevos) {
        if (resultado.none { it.id == juego.id }) {
            resultado.add(juego)
            contadorNuevos++
        }
    }

    println("Se han integrado $contadorNuevos videojuego(s) nuevo(s) tras la importación.")
    return resultado
}

fun submenuConversiones() {
    var volver = false

    while (!volver) {
        println()
        println("--- Conversiones ---")
        println("1. CSV → JSON")
        println("2. CSV → XML")
        println("3. JSON → CSV")
        println("4. JSON → XML")
        println("5. XML → CSV")
        println("6. XML → JSON")
        println("0. Volver")
        print("Elige una opción: ")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> convertir(
                obtenerRutaIni("videojuegos.csv"),
                obtenerRutaFin("videojuegos.json"),
                ::leerCSV,
                ::escribirVideojuegosJSON
            )
            2 -> convertir(
                obtenerRutaIni("videojuegos.csv"),
                obtenerRutaFin("videojuegos.xml"),
                ::leerCSV,
                ::escribirVideojuegosXML
            )
            3 -> convertir(
                obtenerRutaIni("videojuegos.json"),
                obtenerRutaFin("videojuegos.csv"),
                ::leerVideojuegosJSON,
                ::escribirCSV
            )
            4 -> convertir(
                obtenerRutaIni("videojuegos.json"),
                obtenerRutaFin("videojuegos.xml"),
                ::leerVideojuegosJSON,
                ::escribirVideojuegosXML
            )
            5 -> convertir(
                obtenerRutaIni("videojuegos.xml"),
                obtenerRutaFin("videojuegos.csv"),
                ::leerVideojuegosXML,
                ::escribirCSV
            )
            6 -> convertir(
                obtenerRutaIni("videojuegos.xml"),
                obtenerRutaFin("videojuegos.json"),
                ::leerVideojuegosXML,
                ::escribirVideojuegosJSON
            )
            0 -> volver = true
            null -> println("Introduce una opción numérica válida.")
            else -> println("Opción no válida.")
        }
    }
}
