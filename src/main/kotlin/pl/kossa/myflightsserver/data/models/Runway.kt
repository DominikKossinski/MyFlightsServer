package pl.kossa.myflightsserver.data.models

import javax.persistence.*

@Entity
@Table(name = "Runways")
data class Runway(

        @Id
        @GeneratedValue(strategy = GenerationType.AUTO)
        @Column(name = "RunwayId", columnDefinition = "int")
        val runwayId: Int,

        @Column(name = "RunwayName", columnDefinition = "varchar(200)")
        val name: String,

        @Column(name = "Length", columnDefinition = "int")
        val length: Int,

        @Column(name = "Heading", columnDefinition = "int")
        val heading: Int,

        @Column(name = "ILSFrequency", columnDefinition = "varchar(200)")
        val ilsFrequency: String?,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "ImageId", referencedColumnName = "ImageId")
        val image: Image?,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "AirportId", referencedColumnName = "AirportId")
        val airport: Airport
)