import kotlinx.browser.document
import org.w3c.dom.HTMLButtonElement

fun main() {
    val openSetting = document.getElementById("option") as HTMLButtonElement
    openSetting.addEventListener("click", {
        chrome.runtime.openOptionsPage()
    })
}
