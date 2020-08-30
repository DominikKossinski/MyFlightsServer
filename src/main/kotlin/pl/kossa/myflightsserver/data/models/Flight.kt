package pl.kossa.myflightsserver.data.models

import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Flights")
data class Flight(
        @Id
        @Column(name = "FlightId", columnDefinition = "int")
        val flightId: Int,

        @Column(name = "Note", columnDefinition = "varchar")
        val note: String?,

        @Column(name = "Distance", columnDefinition = "varchar")
        val distance: Int?,

        @Column(name = "ImageUrl", columnDefinition = "varchar")
        val imageUrl: String?,

        @Column(name = "StartDate", columnDefinition = "datetime")
        val startDate: Date,

        @Column(name = "EndDate", columnDefinition = "datetime")
        val endDate: Date,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "AirplaneId", referencedColumnName = "AirplaneId")
        val airplane: Airplane,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "DepartureRunwayId", referencedColumnName = "RunwayID")
        val departureRunway: Runway,


        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "ArrivalRunwayId", referencedColumnName = "RunwayID")
        val arrivalRunway: Runway
)