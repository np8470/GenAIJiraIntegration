"use strict";

/**
 * Updates only one Test Case card.
 */
function updateTestCaseCard(updatedTestCase) {
    console.log("Updating");

    if (!updatedTestCase)
        return;

    //------------------------------------
    // Find current card
    //------------------------------------

    const card = document.querySelector(

        `.testcase-item[data-clientid="${updatedTestCase.clientId}"]`

    );

    if (!card)
        return;

    //------------------------------------
    // Save current UI state
    //------------------------------------

    const checkbox =
        card.querySelector(".testcaseCheckbox");

    const checked =
        checkbox ? checkbox.checked : true;

    const collapse =
        card.querySelector(".accordion-collapse");

    const expanded =
        collapse.classList.contains("show");

    //------------------------------------
    // Update in JS model
    //------------------------------------

    const index =
        generatedTestCases.findIndex(

            tc => tc.clientId === updatedTestCase.clientId

        );

    if (index >= 0) {

        generatedTestCases[index] = updatedTestCase;

    }

    //------------------------------------
    // Build NEW HTML
    //------------------------------------

    const wrapper =
        document.createElement("div");

    wrapper.innerHTML =
        buildTestCaseCard(updatedTestCase, index);

    const newCard =
        wrapper.firstElementChild;

    //------------------------------------
    // Replace DOM
    //------------------------------------

    card.replaceWith(newCard);

    //------------------------------------
    // Restore checkbox
    //------------------------------------

    newCard.querySelector(".testcaseCheckbox").checked =
        checked;

    //------------------------------------
    // Restore accordion
    //------------------------------------

    if (expanded) {

        const newCollapse =
            newCard.querySelector(".accordion-collapse");

        bootstrap.Collapse
            .getOrCreateInstance(newCollapse)
            .show();

    }

    //------------------------------------
    // Re-enable sortable
    //------------------------------------

    initializeSortable();

}