package pl.kossa.myflightsserver.data.models.ofp

import com.fasterxml.jackson.annotation.JsonProperty
import org.springframework.data.annotation.Id
import org.springframework.data.mongodb.core.mapping.DBRef
import org.springframework.data.mongodb.core.mapping.Document

@Document(collection = "ofp_fix")
data class OFPFix(
    @Id
    val fixId: String,
    @DBRef
    val ofpWayPoint: OFPWayPoint,
    val viaAirway: String
    //TODO fields
)

enum class OFPWayPointType {
    @JsonProperty("wpt")
    WPT,

    @JsonProperty("ltlg")
    LTGT,

    @JsonProperty("apt")
    APT
}

abstract class OFPWayPoint {
    abstract val ident: String
    abstract val name: String
    abstract val posLat: Float
    abstract val posLong: Float
    abstract val type: OFPWayPointType
}

@Document(collection = "ofp_simple_way_point")
data class OFPSimpleWayPoint(
    @Id
    override val ident: String,
    override val name: String,
    override val posLat: Float,
    override val posLong: Float,
) : OFPWayPoint() {
    override val type: OFPWayPointType = OFPWayPointType.WPT
}

@Document(collection = "ofp_ltgt_way_point")
data class OFPLtgtWayPoint(
    @Id
    val lgtgId: String,
    override val ident: String,
    override val name: String,
    override val posLat: Float,
    override val posLong: Float
) : OFPWayPoint() {
    override val type: OFPWayPointType = OFPWayPointType.LTGT
}

@Document(collection = "ofp_apt_way_point")
data class OFPAptWayPoint(
    @Id
    val aptId: String,
    override val ident: String,
    override val name: String,
    override val posLat: Float,
    override val posLong: Float
) : OFPWayPoint() {
    override val type: OFPWayPointType = OFPWayPointType.APT
}