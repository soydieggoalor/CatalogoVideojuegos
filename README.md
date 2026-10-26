# Catálogo de Videojuegos

Programa de consola en Kotlin para gestionar un catálogo de videojuegos.
Permite trabajar con ficheros de texto plano (CSV), ficheros de intercambio estructurados (XML y JSON) y ficheros binarios de acceso aleatorio (.bin).

## 1. Estructura de datos

### Data Class:

```kotlin
data class Videojuego(
    val id: Int,
    val titulo: String,
    val genero: String,
    val anio: Int,
    val nota: Double
)
```

### Estructura del registro binario:

- **ID**: Int - 4 bytes
- **titulo**: String - 40 bytes (longitud fija)
- **genero**: String - 20 bytes (longitud fija)
- **anio**: Int - 4 bytes
- **nota**: Double - 8 bytes
- **Tamaño Total del Registro**: 4 + 40 + 20 + 4 + 8 = 76 bytes

## 2. Instrucciones de ejecución

- **Requisitos previos**: JDK 17 o superior instalado.
- **Compilación**: Abre el proyecto en IntelliJ IDEA y deja que Gradle sincronice las dependencias.
- **Fichero de datos inicial**: Coloca `videojuegos.csv` en la carpeta `datos_ini` antes de ejecutar.
- **Ejecución de cada práctica**:
  - `Main.kt`: Práctica 2 (Rutas y estructura de directorios).
  - `Main3.kt`: Práctica 3 (Gestión CRUD con CSV).
  - `Main4.kt`: Práctica 4 (Lectura e importación desde XML).
  - `Main5.kt`: Práctica 5 (Lectura e importación desde JSON).
  - `Main6.kt`: Práctica 6 (Conversiones entre formatos).
  - `MainFinal.kt`: Entrega Final (CRUD completo sobre fichero binario con acceso aleatorio).

## 3. Decisiones de diseño

- Elegí CSV como formato inicial porque es sencillo de crear y editar manualmente con cualquier hoja de cálculo.
- Reservé 40 bytes para el título porque algunos títulos largos como "The Legend of Zelda: Breath of the Wild" tienen 38 caracteres.
- Reservé 20 bytes para el género porque los géneros más largos que manejo ("Plataformas") tienen 11 caracteres y quiero margen para futuros géneros.
- Para el fichero binario se utiliza `FileChannel` y `ByteBuffer` calculando la posición exacta de cada registro (múltiplos de 76 bytes) para permitir lectura, modificación in-situ y borrado sin reescribir innecesariamente.
