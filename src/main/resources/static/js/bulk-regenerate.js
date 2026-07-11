"use strict";

document.addEventListener("click", async function (e) {

    if (e.target.id !== "bulkRegenerateBtn")
        return;

    await bulkRegenerateSelected();

});

async function bulkRegenerateSelected() {

    const selected = [];

    document
        .querySelectorAll(".testcaseCheckbox")
        .forEach(cb => {

            if (!cb.checked)
                return;

            const clientId = cb.dataset.clientid;

            const tc = generatedTestCases.find(

                t => t.clientId === clientId

            );

            if (!tc)
                return;

            selected.push({

                index: generatedTestCases.indexOf(tc),

                clientId: tc.clientId,

                testCase: tc

            });

        });

    if (selected.length === 0) {

        showToast(
            "Please select at least one test case.",
            "warning");

        return;

    }

    await regenerateSelected(selected);

}

async function regenerateSelected(selected) {

    const button =
        document.getElementById(
            "bulkRegenerateBtn");

    button.disabled = true;

    button.innerHTML =
        "Generating...";

    try {

        const response =
            await postJson(

                "/api/v1/regenerate-selected",

                {

                    items: selected

                }

            );
        console.log("Bulk regenerate response");

        console.log(response);

        alert(JSON.stringify(response));

        applyBulkRegeneration(response);

        showToast(
            "Selected Test Cases regenerated successfully.");

    }

    catch (e) {

        console.error(e);

        showToast(
            e.message,
            "danger");

    }

    finally {

        button.disabled = false;

        button.innerHTML =
            "🔄 Regenerate Selected";

    }

}

function applyBulkRegeneration(response) {
    console.log(response);

    console.log(response.success);

    console.log(response.items);
    /* if (!response.success)
        return; */

    response.items.forEach(item => {
        console.log(item);
        console.log(item.clientId);
        const index =
            generatedTestCases.findIndex(

                tc => tc.clientId === item.clientId

            );

        if (index >= 0) {

            generatedTestCases[index] =
                item.testCase;

            // Update only this card
            updateTestCaseCard(item.testCase);

        }

    });

}