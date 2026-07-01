// theme.js

const THEME_KEY = "darkMode";

/*
 * Apply saved theme when page loads
 */
function applySavedTheme() {

    const darkModeEnabled =
        localStorage.getItem(THEME_KEY) === "true";

    const body = document.body;

    const switchEl =
        document.getElementById("darkModeSwitch");

    if (darkModeEnabled) {

        body.classList.add("dark-mode");

    } else {

        body.classList.remove("dark-mode");

    }

    if (switchEl) {

        switchEl.checked = darkModeEnabled;

    }

}

/*
 * Toggle theme
 */
function toggleDarkMode() {

    const switchEl =
        document.getElementById("darkModeSwitch");

    const enabled = switchEl.checked;

    if (enabled) {

        document.body.classList.add("dark-mode");

    } else {

        document.body.classList.remove("dark-mode");

    }

    localStorage.setItem(THEME_KEY, enabled);

}

/*
 * Initialize
 */
window.addEventListener("DOMContentLoaded", function () {

    applySavedTheme();

    const switchEl =
        document.getElementById("darkModeSwitch");

    if (switchEl) {

        switchEl.addEventListener("change", toggleDarkMode);

    }

});