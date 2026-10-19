
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
