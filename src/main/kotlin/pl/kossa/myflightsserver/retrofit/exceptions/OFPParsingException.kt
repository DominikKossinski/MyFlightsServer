package pl.kossa.myflightsserver.retrofit.exceptions

class OFPParsingException(
    nodeName: String?,
    fieldName: String
) : Exception("Field '$fieldName' not found in node $nodeName") {
}