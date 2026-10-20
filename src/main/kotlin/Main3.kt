fun main() {
    var catalogo: List<Videojuego> = VideojuegoRepository.leer()

    do {
        println()
        println("===== CATÁLOGO DE VIDEOJUEGOS =====")
        println("1. Gestión CSV")
        println("0. Salir")
        println("====================================")
        print("Elige una opción: ")

        val opcion = readlnOrNull()?.trim()?.toIntOrNull()
        when (opcion) {
            1 -> catalogo = menuCSV(catalogo)
            0 -> println("¡Hasta luego!")
            else -> println("Opción no válida.")
        }
    } while (opcion != 0)
}

fun menuCSV(listaInicial: List<Videojuego>): List<Videojuego> {
    var lista = listaInicial

    do {
        println()
        println("--- Gestión CSV ---")
        println("1. Listar todos los videojuegos")
        println("2. Buscar videojuego por ID")
        println("3. Añadir nuevo videojuego")
        println("4. Modificar nota de un videojuego")
        println("5. Eliminar videojuego")
        println("0. Volver al menú principal")
        print("Elige una opción: ")

        val opcion = readlnOrNull()?.trim()?.toIntOrNull()
        when (opcion) {
            1 -> listarVideojuegos(lista)

            2 -> {
                val id = leerEntero("ID a buscar: ")
                if (id == null) println("ID no válido.") else buscarPorId(lista, id)
            }

            3 -> {
                val nueva = añadirVideojuego(lista)
                if (nueva != lista) {
                    lista = nueva
                    VideojuegoRepository.guardar(lista)
                }
            }

            4 -> {
                val id = leerEntero("ID del videojuego: ")
                if (id == null) {
                    println("ID no válido.")
                } else if (lista.none { it.id == id }) {
                    println("No existe ningún videojuego con ID $id.")
                } else {
                    print("Nueva nota (0.0 - 10.0): ")
                    val nota = readlnOrNull()?.trim()?.replace(',', '.')?.toDoubleOrNull()
                    if (nota == null || nota !in 0.0..10.0) {
                        println("Nota no válida: debe estar entre 0.0 y 10.0.")
                    } else {
                        lista = modificarNota(lista, id, nota)
                        VideojuegoRepository.guardar(lista)
                    }
                }
            }

            5 -> {
                val id = leerEntero("ID a eliminar: ")
                if (id == null) {
                    println("ID no válido.")
                } else {
                    val nueva = eliminarVideojuego(lista, id)
                    if (nueva != lista) {
                        lista = nueva
                        VideojuegoRepository.guardar(lista)
                    }
                }
            }

            0 -> {}
            else -> println("Opción no válida.")
        }
    } while (opcion != 0)

    return lista
}

fun leerEntero(mensaje: String): Int? {
    print(mensaje)
    return readlnOrNull()?.trim()?.toIntOrNull()
}