document.addEventListener("click", async function (e) {

    if (!e.target.classList.contains("regenerateBtn"))
        return;

    const clientId =
        e.target.dataset.clientid;

    const tc =
        generatedTestCases.find(
            t => t.clientId === clientId
        );
        
    if (!tc)
        return;    

    e.target.disabled = true;

    e.target.innerHTML = "Generating...";

    try {

        const regenerated = await postJson(
            "/api/v1/regenerate",
            tc
        );

        /* generatedTestCases[index] = regenerated;

        renderTestCases(generatedTestCases); */
        const position =
            generatedTestCases.findIndex(
                t => t.clientId === clientId
            );

        generatedTestCases[position] = regenerated;

        updateTestCaseCard(regenerated);

        showToast("Test Case regenerated successfully.");

    }
    catch (err) {

        showToast(err.message, "danger");

    }
    finally {

        e.target.disabled = false;

        e.target.innerHTML = "🔄 Regenerate";

    }

});