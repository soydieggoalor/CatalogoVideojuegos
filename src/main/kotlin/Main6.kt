fun main() {
    var videojuegos = VideojuegoRepository.leer()
    var salir = false

    while (!salir) {
        println()
        println("===== CATÁLOGO DE VIDEOJUEGOS =====")
        println("1. Gestión CSV")
        println("2. Leer catálogo desde XML")
        println("3. Leer catálogo desde JSON")
        println("4. Conversiones entre formatos")
        println("0. Salir")
        println("====================================")
        print("Elige una opción: ")

        when (readlnOrNull()?.toIntOrNull()) {
            1 -> {
                videojuegos = menuGestionCSV(videojuegos)
            }

            2 -> {
                val rutaXML = obtenerRutaIni("videojuegos.xml")
                val leidos = leerVideojuegosXML(rutaXML)
                println()
                println("--- Videojuegos leídos desde XML ---")
                listarVideojuegos(leidos)
                videojuegos = integrarCatalogo(videojuegos, leidos)
                VideojuegoRepository.guardar(videojuegos)
            }

            3 -> {
                val rutaJSON = obtenerRutaIni("videojuegos.json")
                val leidos = leerVideojuegosJSON(rutaJSON)
                println()
                println("--- Videojuegos leídos desde JSON ---")
                listarVideojuegos(leidos)
                videojuegos = integrarCatalogo(videojuegos, leidos)
                VideojuegoRepository.guardar(videojuegos)
            }

            4 -> {
                submenuConversiones()
            }

            0 -> salir = true

            null -> println("Introduce una opción numérica válida.")

            else -> println("Opción no válida.")
        }
    }

    println("¡Hasta pronto!")
}
