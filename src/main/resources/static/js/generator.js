/*
==========================================================
 GenAI Jira Test Generator
generator.js

Handles:
- Generate Test Cases
- Render Test Cases
- Select All

Depends on:
- app.js
- ui.js
- Bootstrap 5
==========================================================
*/

"use strict";

/* ======================================================
   Generate Test Cases
====================================================== */

function buildTestCaseCard(tc, index) {

    const stepsHtml = (tc.steps || [])
        .map(step => `
            <div class="input-group mb-2 step-row">

                <span class="input-group-text dragHandle">
                    ☰
                </span>

                <input
                    type="text"
                    class="form-control tc-step"
                    value="${step}">

                <button
                    class="btn btn-outline-danger removeStepBtn"
                    type="button">

                    Remove

                </button>

            </div>
        `)
        .join("");

    return `

<div class="accordion-item testcase-item"

     data-clientid="${tc.clientId}"

     data-title="${(tc.title || "").toLowerCase()}"

     data-description="${(tc.description || "").toLowerCase()}"

     data-priority="${tc.priority || ""}">

    <h2 class="accordion-header">

        <button
            class="accordion-button collapsed"
            type="button"
            data-bs-toggle="collapse"
            data-bs-target="#tc_${tc.clientId}">

            <input
    class="form-check-input me-3 testcaseCheckbox"
    type="checkbox"
    checked
    data-clientid="${tc.clientId}"
    onclick="event.stopPropagation()">

            <div>

    <span class="badge bg-primary me-2">

        TC-${String(index+1).padStart(3,'0')}

    </span>

    <strong>${tc.title}</strong>

</div>

        </button>

    </h2>

    <div

        id="tc_${tc.clientId}"

        class="accordion-collapse collapse">

        <div class="accordion-body">

            <div class="mb-3">

                <label class="form-label">

                    Title

                </label>

                <input

                    class="form-control tc-title"

                    value="${tc.title || ""}">

            </div>

            <div class="mb-3">

                <label class="form-label">

                    Description

                </label>

                <textarea

                    class="form-control tc-description"

                    rows="3">${tc.description || ""}</textarea>

            </div>

            <div class="row">

                <div class="col-md-6">

                    <label class="form-label">

                        Priority

                    </label>

                    <select class="form-select tc-priority">

                        <option ${tc.priority === "High" ? "selected" : ""}>High</option>

                        <option ${tc.priority === "Medium" ? "selected" : ""}>Medium</option>

                        <option ${tc.priority === "Low" ? "selected" : ""}>Low</option>

                    </select>

                </div>

                <div class="col-md-6">

                    <label class="form-label">

                        Type

                    </label>

                    <input

                        class="form-control tc-type"

                        value="${tc.type || ""}">

                </div>

            </div>

            <div class="mt-3">

                <label class="form-label">

                    Precondition

                </label>

                <textarea

                    class="form-control tc-precondition"

                    rows="2">${tc.precondition || ""}</textarea>

            </div>

            <div class="mt-3">

                <label class="form-label">

                    Steps

                </label>

                <div class="stepsContainer">

                    ${stepsHtml}

                </div>

                <button

                    class="btn btn-outline-primary btn-sm addStepBtn mt-2"

                    type="button">

                    + Add Step

                </button>

            </div>

            <div class="mt-3">

                <label class="form-label">

                    Expected Result

                </label>

                <textarea

                    class="form-control tc-expected"

                    rows="2">${tc.expectedResult || ""}</textarea>

            </div>

            <div class="mt-4 d-flex gap-2">

                <button

                    class="btn btn-success saveTestCaseBtn">

                    💾 Save

                </button>

                <button

                    class="btn btn-warning regenerateBtn"

                    data-clientid="${tc.clientId}">

                    🔄 Regenerate

                </button>

            </div>

        </div>

    </div>

</div>

`;

}


async function generate() {

    resetGenerationScreen();

    const storyKey =
        document.getElementById("storyKey").value.trim();

    const type =
        document.getElementById("type").value;

    if (storyKey === "") {

        alert("Please enter Story Key");

        return;

    }

    document.getElementById("generateBtn").disabled = true;

    document.getElementById("loadingDiv").style.display = "block";

    document.getElementById("uploadBtn").style.display = "none";

    try {

        const data = await getJson(
            `/api/v1/generate?storyKey=${encodeURIComponent(storyKey)}&type=${encodeURIComponent(type)}`
        );

        renderTestCases(data.testCases || []);

        document.getElementById("uploadBtn").style.display = "inline-block";

        document.getElementById("exportDropdown").style.display = "inline-block";

        showToast("Test cases generated successfully.");

    }

    catch (e) {

        console.error(e);

        showError(e.message);

    }

    finally {

        document.getElementById("loadingDiv").style.display = "none";

        document.getElementById("generateBtn").disabled = false;

    }

}


/* ======================================================
   Render Test Cases
====================================================== */

function renderTestCases(testCases) {

    generatedTestCases = [...testCases];

    let html = "";

    html += `

<div class="d-flex justify-content-between mb-3">

    <div>

        <input
            id="selectAll"
            type="checkbox"
            checked
            onchange="toggleAll(this)">

        <label for="selectAll">

            <b>Select All</b>

        </label>

    </div>

    <div>

        <button
            id="bulkRegenerateBtn"
            class="btn btn-warning btn-sm">

            🔄 Regenerate Selected

        </button>

    </div>

</div>

`;

    testCases.forEach((tc, index) => {
        html += buildTestCaseCard(tc, index);
    });

    document.getElementById("resultContainer").innerHTML = html;
    initializeSortable();

    document.getElementById("searchCount").innerHTML =
        testCases.length + " Test Cases";

    document.getElementById("uploadBtn").style.display =
        "inline-block";

}


/**
 * Selects or deselects all generated test cases.
 */
/* function toggleAll(selectAll) {

    document
        .querySelectorAll(".testcaseCheckbox")
        .forEach(checkbox => {

            checkbox.checked = selectAll.checked;

        });

} */

/**
 * Filters the generated test cases based on the search text.
 */
/* function filterTestCases() {

    const keyword = document
        .getElementById("searchBox")
        .value
        .trim()
        .toLowerCase();

    let visible = 0;

    document
        .querySelectorAll(".testcase-item")
        .forEach(item => {

            const title =
                (item.dataset.title || "").toLowerCase();

            const description =
                (item.dataset.description || "").toLowerCase();

            if (
                title.includes(keyword) ||
                description.includes(keyword)
            ) {

                item.style.display = "";
                visible++;

            } else {

                item.style.display = "none";

            }

        });

    document.getElementById("searchCount").innerHTML =
        visible + " Test Case(s)";

} */

/**
 * Returns a numeric value for priority sorting.
 * Lower number = higher priority.
 */
/* function priorityValue(priority) {

    if (!priority) {
        return 99;
    }

    switch (priority.toUpperCase()) {

        case "HIGH":
            return 1;

        case "MEDIUM":
            return 2;

        case "LOW":
            return 3;

        default:
            return 99;
    }

} */


/**
 * Returns the Bootstrap badge class for a priority.
 */
/* function priorityBadge(priority) {

    if (!priority) {
        return "bg-secondary";
    }

    switch (priority.toUpperCase()) {

        case "HIGH":
            return "bg-danger";

        case "MEDIUM":
            return "bg-warning text-dark";

        case "LOW":
            return "bg-success";

        default:
            return "bg-secondary";
    }

} */


/**
 * Sorts generated test cases by priority.
 */
/* function sortTestCases() {

    const option =
        document.getElementById("prioritySort").value;

    if (option === "NONE") {

        renderTestCases(generatedTestCases);
        return;

    }

    const sorted = [...generatedTestCases];

    if (option === "HIGH") {

        sorted.sort((a, b) =>
            priorityValue(a.priority) -
            priorityValue(b.priority)
        );

    }

    if (option === "LOW") {

        sorted.sort((a, b) =>
            priorityValue(b.priority) -
            priorityValue(a.priority)
        );

    }

    renderTestCases(sorted);

} */

/**
 * Expands all generated test case accordions.
 */
/* function expandAll() {

    document
        .querySelectorAll(".accordion-collapse")
        .forEach(item => {

            const collapse =
                bootstrap.Collapse.getOrCreateInstance(item);

            collapse.show();

        });

} */


/**
 * Collapses all generated test case accordions.
 */
/* function collapseAll() {

    document
        .querySelectorAll(".accordion-collapse")
        .forEach(item => {

            const collapse =
                bootstrap.Collapse.getOrCreateInstance(item);

            collapse.hide();

        });

} */

/* document.addEventListener("click", function (e) {

    if (!e.target.classList.contains("saveTestCaseBtn"))
        return;

    const card = e.target.closest(".testcase-item");

    const index = card.dataset.index;

    const tc = generatedTestCases[index];

    tc.title = card.querySelector(".tc-title").value;

    tc.description = card.querySelector(".tc-description").value;

    tc.priority = card.querySelector(".tc-priority").value;

    tc.type = card.querySelector(".tc-type").value;

    tc.precondition = card.querySelector(".tc-precondition").value;

    tc.expectedResult = card.querySelector(".tc-expected").value;

    tc.steps = [];

    card.querySelectorAll(".tc-step").forEach(step => {

        tc.steps.push(step.value);

    });

    showToast("Changes saved.");

}); */
/* document.addEventListener("click", function (e) {

    if (!e.target.classList.contains("saveTestCaseBtn"))
        return;

    const card = e.target.closest(".testcase-item");

    updateGeneratedTestCase(card);

    showToast("Changes saved.");

}); */

/* document.addEventListener("click", function (e) {

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

}); */

/* function initializeSortable() {

    document
        .querySelectorAll(".stepsContainer")
        .forEach(container => {

            if (container.dataset.sortable)
                return;

            Sortable.create(container, {

    animation: 200,

    handle: ".dragHandle",

    ghostClass: "dragging",

    draggable: ".step-row",

    onEnd: function () {

        updateGeneratedTestCase(
            container.closest(".testcase-item")
        );

    }

});

            container.dataset.sortable = "true";

        });

} */

/* document.addEventListener("click", async function (e) {

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

}); */

/* function updateGeneratedTestCase(card) {

    const index = parseInt(card.dataset.index);

    const tc = generatedTestCases[index];

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

        tc.steps.push(step.value);

    });

} */

/* document.addEventListener("input", function (e) {

    const card = e.target.closest(".testcase-item");

    if (!card)
        return;

    updateGeneratedTestCase(card);

}); */