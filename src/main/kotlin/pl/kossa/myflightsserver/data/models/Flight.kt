package pl.kossa.myflightsserver.data.models

import com.fasterxml.jackson.annotation.JsonProperty
import java.util.*
import javax.persistence.*

@Entity
@Table(name = "Flights")
data class Flight(
        @Id
        @Column(name = "FlightId", columnDefinition = "int")
        val flightId: Int,

        @Column(name = "Note", columnDefinition = "varchar(200)")
        val note: String?,

        @Column(name = "Distance", columnDefinition = "varchar(200)")
        val distance: Int?,

        @OneToOne(cascade = [CascadeType.ALL])
        @JoinColumn(name = "ImageId", referencedColumnName = "ImageId")
        val image: Image?,

        @Column(name = "StartDate", columnDefinition = "datetime")
        val startDate: Date,

        @Column(name = "EndDate", columnDefinition = "datetime")
        val endDate: Date,

        @Column(name = "UserId", columnDefinition = "varchar(200)")
        @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
        val userId: String,

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