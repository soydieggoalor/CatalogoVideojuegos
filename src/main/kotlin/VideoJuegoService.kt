fun listarVideojuegos(lista: List<Videojuego>) {
    if (lista.isEmpty()) {
        println("El catálogo está vacío.")
        return
    }
    println("--- Catálogo de videojuegos ---")
    lista.forEach { mostrarVideojuego(it) }
}

fun mostrarVideojuego(v: Videojuego) {
    println("  - ID: ${v.id}, Título: ${v.titulo}, Género: ${v.genero}, Año: ${v.anio}, Nota: ${v.nota}")
}

fun buscarPorId(lista: List<Videojuego>, id: Int) {
    val encontrado = lista.find { it.id == id }
    if (encontrado != null) mostrarVideojuego(encontrado)
    else println("No existe ningún videojuego con ID $id.")
}

fun añadirVideojuego(lista: List<Videojuego>): List<Videojuego> {
    print("Título: ")
    val titulo = readlnOrNull()?.trim()
    if (titulo.isNullOrEmpty()) {
        println("El título no puede estar vacío.")
        return lista
    }

    print("Género: ")
    val genero = readlnOrNull()?.trim()
    if (genero.isNullOrEmpty()) {
        println("El género no puede estar vacío.")
        return lista
    }

    print("Año: ")
    val anio = readlnOrNull()?.trim()?.toIntOrNull()
    if (anio == null || anio < 1950 || anio > 2100) {
        println("Año no válido.")
        return lista
    }

    print("Nota (0.0 - 10.0): ")
    val nota = readlnOrNull()?.trim()?.replace(',', '.')?.toDoubleOrNull()
    if (nota == null || nota !in 0.0..10.0) {
        println("Nota no válida: debe estar entre 0.0 y 10.0.")
        return lista
    }

    val nuevoId = (lista.maxOfOrNull { it.id } ?: 0) + 1
    println("Videojuego añadido con ID $nuevoId.")
    return lista + Videojuego(nuevoId, titulo, genero, anio, nota)
}

fun modificarNota(lista: List<Videojuego>, id: Int, nuevaNota: Double): List<Videojuego> {
    if (lista.none { it.id == id }) {
        println("No existe ningún videojuego con ID $id.")
        return lista
    }
    if (nuevaNota !in 0.0..10.0) {
        println("Nota fuera de rango [0.0, 10.0].")
        return lista
    }
    println("Nota actualizada.")
    return lista.map { if (it.id == id) it.copy(nota = nuevaNota) else it }
}

fun eliminarVideojuego(lista: List<Videojuego>, id: Int): List<Videojuego> {
    val videojuego = lista.find { it.id == id }
    if (videojuego == null) {
        println("No existe ningún videojuego con ID $id.")
        return lista
    }
    print("¿Seguro que quieres eliminar \"${videojuego.titulo}\"? (s/n): ")
    val respuesta = readlnOrNull()?.trim()?.lowercase()
    return if (respuesta == "s") {
        println("Videojuego eliminado.")
        lista.filter { it.id != id }
    } else {
        println("Operación cancelada.")
        lista
    }
}