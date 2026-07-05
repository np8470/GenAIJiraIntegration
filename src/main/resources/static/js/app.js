/*
==========================================================
 GenAI Jira Test Generator
 app.js
----------------------------------------------------------
 Shared variables and common utility functions
==========================================================
*/

"use strict";

/* ======================================================
   Global Variables
====================================================== */

let generatedTestCases = [];


/* ======================================================
   Toast Notification
====================================================== */

function showToast(message, type = "primary") {

    const toastBody =
        document.getElementById("toastBody");

    if (toastBody) {

        toastBody.innerHTML = message;

        toastBody.className =
            "toast-body text-" + type;

    }

    const toast =
        bootstrap.Toast.getOrCreateInstance(
            document.getElementById("statusToast")
        );

    toast.show();

}


/* ======================================================
   Copy Issue Key
====================================================== */

function copyIssueKey(issueKey) {

    navigator.clipboard.writeText(issueKey)
        .then(() => {

            showToast(issueKey + " copied.");

        })
        .catch(err => {

            console.error("Clipboard Error", err);

        });

}


/* ======================================================
   Scroll to Upload Summary
====================================================== */

function scrollToSuccess() {

    const successCard =
        document.getElementById("successAlert");

    if (!successCard) return;

    successCard.scrollIntoView({

        behavior: "smooth",

        block: "start"

    });

}


/* ======================================================
   Enable / Disable Upload Controls
====================================================== */

function setUploading(uploading) {

    const generateBtn =
        document.getElementById("generateBtn");

    const uploadBtn =
        document.getElementById("uploadBtn");

    if (generateBtn) {

        generateBtn.disabled = uploading;

    }

    if (!uploadBtn) {

        return;

    }

    uploadBtn.disabled = uploading;

    if (uploading) {

        uploadBtn.innerHTML = `

            <span class="spinner-border spinner-border-sm"></span>

            Uploading...

        `;

    } else {

        uploadBtn.innerHTML =
            "Upload Test Cases to Jira";

    }

}


/* ======================================================
   Upload Progress Bar
====================================================== */

function updateProgress(percent) {

    const bar =
        document.getElementById("uploadProgressBar");

    if (!bar) return;

    bar.style.width = percent + "%";

    bar.innerHTML = percent + "%";

    if (percent >= 100) {

        bar.classList.remove("bg-primary");

        bar.classList.add("bg-success");

    } else {

        bar.classList.remove("bg-success");

        bar.classList.add("bg-primary");

    }

}


/* ======================================================
   Open All Jira Links
====================================================== */

function openAllJiraLinks() {

    document
        .querySelectorAll("#jiraLinksContainer a")
        .forEach(link => {

            window.open(
                link.href,
                "_blank"
            );

        });

}


/* ======================================================
   Common Error Handler
====================================================== */

function showError(message) {

    const errorAlert =
        document.getElementById("errorAlert");

    if (!errorAlert) {

        alert(message);

        return;

    }

    errorAlert.style.display = "block";

    errorAlert.innerHTML = message;

}


/* ======================================================
   Hide Error
====================================================== */

function hideError() {

    const errorAlert =
        document.getElementById("errorAlert");

    if (errorAlert) {

        errorAlert.style.display = "none";

    }

}


/* ======================================================
   Hide Success Card
====================================================== */

function hideSuccess() {

    const success =
        document.getElementById("successAlert");

    if (success) {

        success.style.display = "none";

    }

}


/* ======================================================
   Reset Upload Summary
====================================================== */

function resetUploadSummary() {

    const uploadSummary =
        document.getElementById("uploadSummary");

    if (uploadSummary) {

        uploadSummary.style.display = "none";

    }

}


/* ======================================================
   Reset Generate Screen
====================================================== */

function resetGenerationScreen() {

    hideError();

    hideSuccess();

    resetUploadSummary();

    const uploadCard =
        document.getElementById("uploadProgressCard");

    if (uploadCard) {

        uploadCard.style.display = "none";

    }

}


/* ======================================================
   HTTP GET Helper
====================================================== */

/* ======================================================
   HTTP GET Helper
====================================================== */

async function getJson(url) {

    const response = await fetch(url, {

        headers: {

            "Accept": "application/json"

        }

    });

    if (!response.ok) {

        const error = await response.json();

        throw new Error(error.message);

    }

    return await response.json();

}


/* ======================================================
   HTTP POST Helper
====================================================== */

/* ======================================================
   HTTP POST Helper
====================================================== */

async function postJson(url, body) {

    const response = await fetch(url, {

        method: "POST",

        headers: {

            "Content-Type": "application/json",

            "Accept": "application/json"

        },

        body: JSON.stringify(body)

    });

    if (!response.ok) {

        const error = await response.json();

        throw new Error(error.message);

    }

    return await response.json();

}

function initApp() {

    // Generate button
    document.getElementById("generateBtn")
        .addEventListener("click", generate);

    // Upload button
    document.getElementById("uploadBtn")
        .addEventListener("click", uploadToJira);

    // Search
    document.getElementById("searchBox")
        .addEventListener("keyup", filterTestCases);

    // Sort dropdown
    document.getElementById("prioritySort")
        .addEventListener("change", sortTestCases);

    // Expand / collapse buttons
    document.getElementById("expandAllBtn")
        ?.addEventListener("click", expandAll);

    document.getElementById("collapseAllBtn")
        ?.addEventListener("click", collapseAll);

    // Select all checkbox will be dynamic → handled in renderTestCases
}

window.addEventListener("DOMContentLoaded", function () {

    initApp();        // bind all events
    initTheme();     // from theme.js
}); 