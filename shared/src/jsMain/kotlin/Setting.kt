import kotlinx.serialization.Serializable

@Serializable
data class Setting(
    val replaceWordPatternRef: Map<String, Map<String, String>>? = null,
    val siteList: List<Site>
)

@Serializable
data class Site(
    val urlPattern: String,
    val replaceTargetList: List<ReplaceTarget>
)

@Serializable
data class ReplaceTarget(
    val selectors: List<String>,
    val replaceWordPatternRef: String? = null,
    val replaceWordPattern: Map<String, String>? = null
)
