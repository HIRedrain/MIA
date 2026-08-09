package mia.dto

data class PostCreateRequest(
    val postURL: String,
    val productName: String,
    val productURL: String,
    val keyword: String,
    val dmMessage: String
) {
    override fun toString(): String {
        return "PostCreateRequest (postURL : $postURL, productName = $productName, \n   productURL = $productURL, \n   keyword = $keyword, \n   dmMessage = $dmMessage, "
    }
}
