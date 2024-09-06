fun main() {
    chrome.runtime.onInstalled.addListener { detail ->
        console.log("onInstalled", detail)
        if (detail.reason == "install") {
            chrome.runtime.openOptionsPage()
        }
    }
}
