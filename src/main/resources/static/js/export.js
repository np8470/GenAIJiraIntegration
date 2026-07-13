"use strict";


/**
 * Returns file extension.
 */
function getFileExtension(format) {

    switch (format) {

        case "excel":
            return "xlsx";

        case "csv":
            return "csv";

        case "pdf":
            return "pdf";

        case "word":
            return "docx";

        default:
            return "dat";

    }

}

/**
 * Synchronize all edited cards
 * into generatedTestCases.
 */
function syncAllEditedTestCases() {

    document

        .querySelectorAll(".testcase-item")

        .forEach(card => {

            updateGeneratedTestCase(card);

        });

}

/**
 * Export currently edited test cases.
 *
 * Supported formats:
 * excel
 * csv
 * pdf
 * word
 */
async function exportTestCases(format) {

    if (!generatedTestCases || generatedTestCases.length === 0) {

        showToast("Please generate test cases first.", "warning");
        return;

    }

    //---------------------------------------
    // Update generatedTestCases from UI
    //---------------------------------------

    syncAllEditedTestCases();

    //---------------------------------------
    // Build request
    //---------------------------------------

    const request = {

        storyKey: document.getElementById("storyKey").value.trim(),

        testCases: generatedTestCases

    };

    //---------------------------------------
    // Disable export button
    //---------------------------------------

    const exportBtn =
        document.getElementById("exportDropdown");

    exportBtn.disabled = true;

    try {

        const response = await fetch(

            `/api/v1/export/${format}`,

            {

                method: "POST",

                headers: {

                    "Content-Type": "application/json"

                },

                body: JSON.stringify(request)

            }

        );

        if (!response.ok) {

            throw new Error("Export failed.");

        }

        //---------------------------------------
        // Download file
        //---------------------------------------

        const blob = await response.blob();

        const url =
            window.URL.createObjectURL(blob);

        const link =
            document.createElement("a");

        link.href = url;

        //---------------------------------------
        // Filename
        //---------------------------------------

        const storyKey =
            request.storyKey || "TestCases";

        const extension =
            getFileExtension(format);

        link.download =
            storyKey + "_TestCases." + extension;

        document.body.appendChild(link);

        link.click();

        link.remove();

        window.URL.revokeObjectURL(url);

        showToast(

            format.toUpperCase() + " exported successfully."

        );

    }

    catch (err) {

        console.error(err);

        showToast(err.message, "danger");

    }

    finally {

        exportBtn.disabled = false;

    }

}