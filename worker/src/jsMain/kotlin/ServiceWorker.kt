import kotlin.js.json

fun main() {
    chrome.runtime.onInstalled.addListener { detail ->
        console.log("onInstalled", detail)
        chrome.storage.local.set(json("foo" to "bar")) {
            console.log("local storage set")
        }
    }
}
