// ui.js

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

function copyIssueKey(issueKey) {
    navigator.clipboard.writeText(issueKey);
    showToast(issueKey + " copied.");
}

function updateProgress(percent) {
    const bar = document.getElementById("uploadProgressBar");

    bar.style.width = percent + "%";
    bar.innerHTML = percent + "%";

    if (percent == 100) {
        bar.classList.remove("bg-primary");
        bar.classList.add("bg-success");
    }
}

function scrollToSuccess() {
    document.getElementById("successAlert")
        .scrollIntoView({
            behavior: "smooth",
            block: "start"
        });
}

function setUploading(uploading) {
    document.getElementById("generateBtn").disabled = uploading;
    document.getElementById("uploadBtn").disabled = uploading;

    if (uploading) {
        document.getElementById("uploadBtn").innerHTML = `
            <span class="spinner-border spinner-border-sm"></span>
            Uploading...
        `;
    } else {
        document.getElementById("uploadBtn").innerHTML =
            "Upload Test Cases to Jira";
    }
}