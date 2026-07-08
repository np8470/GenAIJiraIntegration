document.addEventListener("click", async function (e) {

    if (!e.target.classList.contains("regenerateBtn"))
        return;

    const index = e.target.dataset.index;

    const tc = generatedTestCases[index];

    e.target.disabled = true;

    e.target.innerHTML = "Generating...";

    try {

        const regenerated = await postJson(
            "/api/v1/regenerate",
            tc
        );

        generatedTestCases[index] = regenerated;

        renderTestCases(generatedTestCases);

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