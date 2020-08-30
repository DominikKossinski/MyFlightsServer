package pl.kossa.myflightsserver.data.models

import javax.persistence.*

@Entity
@Table(name = "Runways")
data class Runway(

        @Id
        @Column(name = "RunwayId", columnDefinition = "int")
        val runwayId: Int,

        @Column(name = "Name", columnDefinition = "varchar")
        val name: String,

        @Column(name = "Length", columnDefinition = "varchar")
        val length: Int,

        @Column(name = "Heading", columnDefinition = "int")
        val heading: Int,

        @Column(name = "ILSFrequency", columnDefinition = "varchar")
        val ilsFrequency: String?,

        @Column(name = "ImageUrl", columnDefinition = "varchar")
        val imageUrl: String?,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "AirportId", referencedColumnName = "AirportId")
        val airport: Airport
)