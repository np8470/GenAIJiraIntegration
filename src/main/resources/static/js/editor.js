function updateGeneratedTestCase(card) {

    const clientId =
        card.dataset.clientid;

    const tc =
        generatedTestCases.find(
            t => t.clientId === clientId
        );

    if (!tc)
        return;

    tc.title = card.querySelector(".tc-title").value;

    tc.description =
        card.querySelector(".tc-description").value;

    tc.priority =
        card.querySelector(".tc-priority").value;

    tc.type =
        card.querySelector(".tc-type").value;

    tc.precondition =
        card.querySelector(".tc-precondition").value;

    tc.expectedResult =
        card.querySelector(".tc-expected").value;

    tc.steps = [];

    card.querySelectorAll(".tc-step").forEach(step => {

        const value =
            step.value.trim();

        if (value.length > 0) {

            tc.steps.push(value);

        }

    });

}

document.addEventListener("click", function (e) {

    if (!e.target.classList.contains("saveTestCaseBtn"))
        return;

    const card = e.target.closest(".testcase-item");

    updateGeneratedTestCase(card);

    showToast("Changes saved.");
    updateTestCaseCard(tc);

});

document.addEventListener("click", function (e) {

    if (!e.target.classList.contains("addStepBtn"))
        return;

    const container =
        e.target.parentElement.querySelector(".stepsContainer");

    container.insertAdjacentHTML(
        "beforeend",
        `
    <div class="input-group mb-2 step-row">

        <span
            class="input-group-text dragHandle">

            ☰

        </span>

        <input
            type="text"
            class="form-control tc-step"
            placeholder="Enter Step">

        <button
            class="btn btn-outline-danger removeStepBtn"
            type="button">

            Remove

        </button>

    </div>
`
    );

    initializeSortable();
    updateGeneratedTestCase(
        e.target.closest(".testcase-item")
    );

});

document.addEventListener("click", function (e) {

    if (!e.target.classList.contains("removeStepBtn"))
        return;

    const row = e.target.closest(".input-group");

    if (row) {

        row.remove();

        updateGeneratedTestCase(
            e.target.closest(".testcase-item")
        );

    }

});

document.addEventListener("input", function (e) {

    const card = e.target.closest(".testcase-item");

    if (!card)
        return;

    updateGeneratedTestCase(card);

});