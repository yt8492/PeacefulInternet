@file:JsQualifier("chrome.runtime")
package chrome.runtime

external val onInstalled: OnInstalled

external interface OnInstalled {
    fun addListener(listener: (OnInstalledDetail) -> Unit)
}

external interface OnInstalledDetail {
    val id: String?
    val previousVersion: String?
    val reason: String
}
