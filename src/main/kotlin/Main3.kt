
fun main() {
    var videojuegos = VideojuegoRepository.leer()

    var salir = false

    while (!salir) {
        println()
        println("===== CATÁLOGO DE VIDEOJUEGOS =====")
        println("1. Gestión CSV")
        println("0. Salir")
        println("====================================")
        print("Elige una opción: ")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> {
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
                                    videojuegos = modificarNota(
                                        videojuegos, id, nota
                                    )
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
            }

            0 -> salir = true

            null -> println("Introduce una opción numérica válida.")

            else -> println("Opción no válida.")
        }
    }

    println("¡Hasta pronto!")
}
