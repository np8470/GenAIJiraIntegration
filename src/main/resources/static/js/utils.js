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

function expandAll() {

    document
        .querySelectorAll(".accordion-collapse")
        .forEach(item => {

            const collapse =
                bootstrap.Collapse.getOrCreateInstance(item);

            collapse.show();

        });

}

function collapseAll() {

    document
        .querySelectorAll(".accordion-collapse")
        .forEach(item => {

            const collapse =
                bootstrap.Collapse.getOrCreateInstance(item);

            collapse.hide();

        });

}