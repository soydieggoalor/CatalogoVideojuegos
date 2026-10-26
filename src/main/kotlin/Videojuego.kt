import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlElementWrapper
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlProperty
import com.fasterxml.jackson.dataformat.xml.annotation.JacksonXmlRootElement
import kotlinx.serialization.Serializable

@Serializable
data class Videojuego(
    @JacksonXmlProperty(localName = "id")
    val id: Int = 0,
    @JacksonXmlProperty(localName = "titulo")
    val titulo: String = "",
    @JacksonXmlProperty(localName = "genero")
    val genero: String = "",
    @JacksonXmlProperty(localName = "anio")
    val anio: Int = 0,
    @JacksonXmlProperty(localName = "nota")
    val nota: Double = 0.0
)
@JacksonXmlRootElement(localName = "videojuegos")
data class VideojuegosXML(
    @JacksonXmlElementWrapper(useWrapping = false)
    @JacksonXmlProperty(localName = "videojuego")
    val listaVideojuegos: List<Videojuego> = emptyList()
)