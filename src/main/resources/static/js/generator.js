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

        document.getElementById("uploadBtn").style.display =
            "inline-block";

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

    /* ------------------------------------------
       Select All Checkbox
    ------------------------------------------- */

    html += `
        <div class="mb-3">

            <input
                type="checkbox"
                id="selectAll"
                checked
                onchange="toggleAll(this)">

            <label for="selectAll">

                <b>Select All</b>

            </label>

        </div>
    `;


    /* ------------------------------------------
       Test Case Cards
    ------------------------------------------- */

    testCases.forEach((tc, index) => {

        html += `

<div
    class="accordion-item testcase-item"

    data-title="${(tc.title || "").toLowerCase()}"

    data-description="${(tc.description || "").toLowerCase()}"

    data-priority="${tc.priority}">

    <h2 class="accordion-header">

        <button

            class="accordion-button collapsed"

            type="button"

            data-bs-toggle="collapse"

            data-bs-target="#tc${index}">

            <input

                class="form-check-input me-3 testcaseCheckbox"

                type="checkbox"

                checked

                value="${index}"

                onclick="event.stopPropagation()">

            <span class="fw-semibold">

                ${tc.title}

            </span>

        </button>

    </h2>

    <div

        id="tc${index}"

        class="accordion-collapse collapse"

        data-bs-parent="">

        <div class="accordion-body">

            <p>

                <b>Description</b>

                <br>

                ${tc.description}

            </p>

            <p>

                <b>Priority :</b>

                <span class="badge ${priorityBadge(tc.priority)}">

                    ${tc.priority}

                </span>

            </p>

            <p>

                <b>Type :</b>

                ${tc.type}

            </p>

            <p>

                <b>Precondition</b>

                <br>

                ${tc.precondition}

            </p>

            <b>Steps</b>

            <ol>
                        ${(tc.steps || [])
                .map(step => `<li>${step}</li>`)
                .join("")
            }

            </ol>

            <p>

                <b>Expected Result</b>

                <br>

                ${tc.expectedResult}

            </p>

        </div>

    </div>

</div>

`;
    });

    document.getElementById("resultContainer").innerHTML = html;

    document.getElementById("searchCount").innerHTML =
        testCases.length + " Test Cases";

    document.getElementById("uploadBtn").style.display =
        "inline-block";

}

/**
 * Selects or deselects all generated test cases.
 */
function toggleAll(selectAll) {

    document
        .querySelectorAll(".testcaseCheckbox")
        .forEach(checkbox => {

            checkbox.checked = selectAll.checked;

        });

}

/**
 * Filters the generated test cases based on the search text.
 */
function filterTestCases() {

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

}

/**
 * Returns a numeric value for priority sorting.
 * Lower number = higher priority.
 */
function priorityValue(priority) {

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

}


/**
 * Returns the Bootstrap badge class for a priority.
 */
function priorityBadge(priority) {

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

}


/**
 * Sorts generated test cases by priority.
 */
function sortTestCases() {

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

}

/**
 * Expands all generated test case accordions.
 */
function expandAll() {

    document
        .querySelectorAll(".accordion-collapse")
        .forEach(item => {

            const collapse =
                bootstrap.Collapse.getOrCreateInstance(item);

            collapse.show();

        });

}


/**
 * Collapses all generated test case accordions.
 */
function collapseAll() {

    document
        .querySelectorAll(".accordion-collapse")
        .forEach(item => {

            const collapse =
                bootstrap.Collapse.getOrCreateInstance(item);

            collapse.hide();

        });

}