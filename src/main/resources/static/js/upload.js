/**
 * Uploads the selected test cases to Jira.
 */
async function uploadToJira() {

    const startTime = Date.now();

    document.getElementById("successAlert").style.display = "none";
    document.getElementById("errorAlert").style.display = "none";

    const selected = [];

    document
        .querySelectorAll(".testcaseCheckbox")
        .forEach(cb => {

            if (cb.checked) {
                selected.push(parseInt(cb.value, 10));
            }

        });

    if (selected.length === 0) {

        showToast("Please select at least one test case.");

        return;

    }

    // Disable buttons while uploading
    document.getElementById("uploadBtn").disabled = true;
    document.getElementById("generateBtn").disabled = true;

    // Show upload progress
    document.getElementById("uploadProgressContainer").style.display =
        "block";

    updateProgress(10);

    try {

        const selectedTestCases = [];

        selected.forEach(index => {

            selectedTestCases.push(generatedTestCases[index]);

        });

        const request = {

            storyKey: document.getElementById("storyKey").value.trim(),

            testCases: selectedTestCases

        };

        const result = await postJson(

            "/api/v1/upload",

            request

        );

        updateProgress(60);

        updateProgress(100);

        if (!result.success) {

            throw new Error(result.message);

        }

        // Upload summary
        document.getElementById("uploadedCount").innerHTML =
            result.uploadedCount;

        document.getElementById("failedCount").innerHTML =
            result.failedCount;

        // Jira links returned by backend
        const jiraLinks = result.jiraLinks || [];

        let html = "";

        if (jiraLinks.length > 0) {

            html += "<h5 class='mb-3'>Created Jira Test Cases</h5>";

            html += "<button ";
            html += "class='btn btn-primary btn-sm mb-3' ";
            html += "onclick='openAllJiraLinks()'>";
            html += "Open All Test Cases";
            html += "</button>";

            html += "<ul class='list-group'>";
            jiraLinks.forEach(link => {

                const issueKey =
                    link.substring(link.lastIndexOf("/") + 1);

                html += `

<li class="list-group-item">

    <div class="d-flex justify-content-between align-items-center mb-2">

        <div>

            ✅

            <a href="${link}" target="_blank">

                ${issueKey}

            </a>

        </div>

        <div>

        </div>

    </div>

</li>

`;

            });

            html += "</ul>";

        }

        document.getElementById("jiraLinksContainer").innerHTML =
            html;

        document.getElementById("successAlert").style.display =
            "block";

        const duration =
            ((Date.now() - startTime) / 1000).toFixed(2);

        document.getElementById("uploadDuration").innerHTML =
            duration + " sec";

        scrollToSuccess();

        showToast(result.message);

    } catch (e) {

        document.getElementById("errorAlert").style.display = "block";
        document.getElementById("errorAlert").innerHTML = e.message;

        showToast(e.message, "danger");

    } finally {

        document.getElementById("uploadBtn").disabled = false;
        document.getElementById("generateBtn").disabled = false;

        setTimeout(() => {

            document.getElementById("uploadProgressContainer").style.display = "none";

            updateProgress(0);

        }, 800);

    }

} // ✅ uploadToJira() ends here


/**
 * Opens all Jira links in new browser tabs.
 */
function openAllJiraLinks() {

    document
        .querySelectorAll("#jiraLinksContainer a")
        .forEach(link => {

            window.open(link.href, "_blank");

        });

}