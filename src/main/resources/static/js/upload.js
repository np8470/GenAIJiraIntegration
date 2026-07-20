/**
 * Uploads the selected test cases to Jira.
 */
async function uploadToJira() {

    const startTime = Date.now();

    document.getElementById("successAlert").style.display = "none";
    document.getElementById("errorAlert").style.display = "none";

    //---------------------------------------
    // Get selected Client IDs
    //---------------------------------------
    const selectedClientIds = [];

    document
        .querySelectorAll(".testcaseCheckbox:checked")
        .forEach(cb => {

            selectedClientIds.push(cb.dataset.clientid);

        });

    if (selectedClientIds.length === 0) {

        showToast("Please select at least one test case.");

        return;

    }

    //---------------------------------------
    // Disable buttons
    //---------------------------------------
    document.getElementById("uploadBtn").disabled = true;
    document.getElementById("generateBtn").disabled = true;

    //---------------------------------------
    // Show progress
    //---------------------------------------
    document.getElementById("uploadProgressContainer").style.display = "block";

    updateProgress(10);

    try {

        //---------------------------------------
        // Build selected TestCase objects
        //---------------------------------------
        //---------------------------------------
// Build selected database IDs
//---------------------------------------

const selectedIds = [];

selectedClientIds.forEach(clientId => {

    const tc = generatedTestCases.find(

        t => t.clientId === clientId

    );

    if (tc && tc.id) {

        selectedIds.push(Number(tc.id));

    }

});

if (selectedIds.length === 0) {

    throw new Error("No valid test cases selected.");

}

//---------------------------------------
// Build request
//---------------------------------------

const request = {

    storyKey: document.getElementById("storyKey").value.trim(),

    testCaseIds: selectedIds

};


        console.log("Uploading Request:", request);

        updateProgress(30);

        //---------------------------------------
        // Call backend
        //---------------------------------------
        const result = await postJson(

            "/api/v1/upload",

            request

        );

        updateProgress(100);

        if (!result.success) {

            throw new Error(result.message);

        }

        //---------------------------------------
        // Upload summary
        //---------------------------------------
        document.getElementById("uploadedCount").innerHTML =
            result.uploadedCount;

        document.getElementById("failedCount").innerHTML =
            result.failedCount;

        //---------------------------------------
        // Render Jira links
        //---------------------------------------
        const jiraLinks = result.jiraLinks || [];

        let html = "";

        if (jiraLinks.length > 0) {

            html += "<h5 class='mb-3'>Created Jira Test Cases</h5>";

            html += `
                <button
                    class="btn btn-primary btn-sm mb-3"
                    onclick="openAllJiraLinks()">
                    Open All Test Cases
                </button>
            `;

            html += "<ul class='list-group'>";

            jiraLinks.forEach(link => {

                const issueKey =
                    link.substring(link.lastIndexOf("/") + 1);

                html += `
                    <li class="list-group-item">
                        <div class="d-flex justify-content-between align-items-center">
                            <div>
                                ✅
                                <a href="${link}" target="_blank">
                                    ${issueKey}
                                </a>
                            </div>
                        </div>
                    </li>
                `;

            });

            html += "</ul>";

        }

        document.getElementById("jiraLinksContainer").innerHTML = html;

        document.getElementById("successAlert").style.display = "block";

        const duration =
            ((Date.now() - startTime) / 1000).toFixed(2);

        document.getElementById("uploadDuration").innerHTML =
            duration + " sec";

        scrollToSuccess();

        showToast(result.message);

    }
    catch (e) {

        console.error(e);

        document.getElementById("errorAlert").style.display = "block";
        document.getElementById("errorAlert").innerHTML = e.message;

        showToast(e.message, "danger");

    }
    finally {

        document.getElementById("uploadBtn").disabled = false;
        document.getElementById("generateBtn").disabled = false;

        setTimeout(() => {

            document.getElementById("uploadProgressContainer").style.display = "none";

            updateProgress(0);

        }, 800);

    }

}


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