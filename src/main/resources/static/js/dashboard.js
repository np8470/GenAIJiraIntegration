/*********************************************************************
 *
 * TestPilot AI Enterprise Dashboard
 * dashboard.js
 *
 * Part 1
 * -----------------------------------
 * ✓ Initialization
 * ✓ Dashboard Statistics
 * ✓ Utility Functions
 * ✓ Common Chart Options
 * ✓ AI Overview Chart
 *
 *********************************************************************/

"use strict";

/*==========================================================
=            Global Variables                              =
==========================================================*/

let dashboardStats = null;

let usageChart = null;
let uploadChart = null;
let priorityChart = null;
let reportTypeChart = null;

/*==========================================================
=            DOM Ready                                     =
==========================================================*/

document.addEventListener("DOMContentLoaded", () => {

    console.log("Loading Enterprise Dashboard...");

    loadDashboardStatistics();

});

/*==========================================================
=            Load Hidden Statistics                        =
==========================================================*/

function loadDashboardStatistics() {

    const statsElement = document.getElementById("dashboardStats");

    if (!statsElement) {

        console.error("dashboardStats div not found.");

        return;
    }

    dashboardStats = {

        totalGenerations:
            Number(statsElement.dataset.total || 0),

        uploadedToJira:
            Number(statsElement.dataset.uploaded || 0),

        successfulRuns:
            Number(statsElement.dataset.success || 0),

        failedRuns:
            Number(statsElement.dataset.failed || 0),

        ui:
            Number(statsElement.dataset.ui),

        api:
            Number(statsElement.dataset.api),

        selenium:
            Number(statsElement.dataset.selenium),

        high:
            Number(statsElement.dataset.high),

        medium:
            Number(statsElement.dataset.medium),

        low:
            Number(statsElement.dataset.low)

    };

    console.table(dashboardStats);

    initializeCharts();

}
/*****************************************************************
 * Animate KPI Cards
 *****************************************************************/

window.addEventListener("load", () => {

    animateCounter("totalGenerations", dashboardStats.totalGenerations);

    animateCounter("uploadedToJira", dashboardStats.uploadedToJira);

    animateCounter("successRuns", dashboardStats.successfulRuns);

    animateCounter("failedRuns", dashboardStats.failedRuns);

});

/*==========================================================
=            Utility Functions                             =
==========================================================*/

function destroyChart(chart) {

    if (chart) {

        chart.destroy();

    }

}

function createGradient(ctx, color1, color2) {

    const gradient = ctx.createLinearGradient(0, 0, 0, 400);

    gradient.addColorStop(0, color1);

    gradient.addColorStop(1, color2);

    return gradient;

}

/*==========================================================
=            Common Chart Options                          =
==========================================================*/

const commonOptions = {

    responsive: true,

    maintainAspectRatio: false,

    plugins: {

        legend: {

            position: "bottom",

            labels: {

                usePointStyle: true,

                padding: 20

            }

        }

    }

};

/*==========================================================
=            Initialize Dashboard Charts                   =
==========================================================*/

function initializeCharts() {

    renderOverviewChart();

    renderUploadChart();

    loadChartData();

    loadRecentGenerations();

    updateLastRefresh();

    startAutoRefresh();


}

/*==========================================================
=            AI Overview Chart                             =
==========================================================*/

function renderOverviewChart() {

    destroyChart(usageChart);

    const canvas = document.getElementById("usageChart");

    if (!canvas)
        return;

    const ctx = canvas.getContext("2d");

    const blueGradient =
        createGradient(
            ctx,
            "#4f8dfd",
            "#0d6efd"
        );

    usageChart = new Chart(ctx, {

        type: "bar",

        data: {

            labels: [

                "Generations",

                "Uploaded",

                "Successful",

                "Failed"

            ],

            datasets: [

                {

                    label: "Dashboard Statistics",

                    data: [

                        dashboardStats.totalGenerations,

                        dashboardStats.uploadedToJira,

                        dashboardStats.successfulRuns,

                        dashboardStats.failedRuns

                    ],

                    backgroundColor: [

                        blueGradient,

                        "#198754",

                        "#20c997",

                        "#dc3545"

                    ],

                    borderRadius: 10,

                    borderWidth: 1

                }

            ]

        },

        options: {

            ...commonOptions,

            scales: {

                y: {

                    beginAtZero: true,

                    ticks: {

                        precision: 0

                    }

                }

            }

        }

    });

}

/*==========================================================
=            Upload Distribution Chart                     =
==========================================================*/

function renderUploadChart() {

    destroyChart(uploadChart);

    const canvas =
        document.getElementById("uploadChart");

    if (!canvas)
        return;

    const ctx =
        canvas.getContext("2d");

    const pending =

        dashboardStats.totalGenerations -

        dashboardStats.uploadedToJira;

    uploadChart =

        new Chart(ctx, {

            type: "doughnut",

            data: {

                labels: [

                    "Uploaded to Jira",

                    "Pending Upload"

                ],

                datasets: [

                    {

                        data: [

                            dashboardStats.uploadedToJira,

                            pending

                        ],

                        backgroundColor: [

                            "#198754",

                            "#ffc107"

                        ],

                        hoverOffset: 15

                    }

                ]

            },

            options: {

                ...commonOptions,

                cutout: "70%"

            }

        });

}

/*********************************************************************
 * Load Analytics Charts
 *********************************************************************/

async function loadChartData() {

    try {

        const response = await fetch("/api/dashboard/charts");

        if (!response.ok) {

            throw new Error("Unable to load chart data.");

        }

        const chartData = await response.json();

        console.log("Dashboard Chart Data");

        console.table(chartData);

        renderPriorityChart(chartData);

        renderReportTypeChart(chartData);

    }

    catch (error) {

        console.error(error);

    }

}

/*********************************************************************
 * Priority Distribution
 *********************************************************************/

function renderPriorityChart(data) {

    destroyChart(priorityChart);

    const pieColors = [
        "#ef4444",
        "#f59e0b",
        "#10b981"
    ];

    const canvas = document.getElementById("priorityChart");


    if (!canvas) {

        return;

    }

    priorityChart = new Chart(canvas, {

        type: "pie",

        data: {

            labels: [

                "High",

                "Medium",

                "Low"

            ],

            datasets: [

                {

                    data: [

                        data.highPriority,

                        data.mediumPriority,

                        data.lowPriority

                    ],

                    backgroundColor: pieColors,

                    borderColor: "#ffffff",

                    borderWidth: 2,
                    
                    hoverOffset: 8


                }

            ]

        },

        options: {

            responsive: true,

            plugins: {

                legend: {

                    position: "bottom",

                    labels: {
                        boxWidth: 14,
                        padding: 12
                    }

                }

            }

        }

    });

}


/*********************************************************************
 * Report Type Distribution
 *********************************************************************/

function renderReportTypeChart(data) {

    destroyChart(reportTypeChart);

    const canvas = document.getElementById("reportTypeChart");


    if (!canvas) {

        return;

    }

    reportTypeChart = new Chart(canvas, {

        type: "bar",

        data: {

            labels: [

                "UI",

                "API",

                "Selenium"

            ],

            datasets: [

                {

                    label: "Generated Reports",

                    data: [

                        data.ui,

                        data.api,

                        data.selenium

                    ],

                    backgroundColor: [

                        "#2563eb",

                        "#06b6d4",

                        "#8b5cf6"

                    ],
                    borderRadius:8,

                    maxBarThickness:35
                }

            ]

        },

        options: {

            responsive: true,

            plugins: {

                legend: {

                    display: false

                }

            },
            layout:{

                padding:15

            },

                scales: {

                    y: {

                        beginAtZero: true,

                        ticks: {

                            precision: 0,
                            stepSize: 5

                        },
                        grid: {
                            color: "#f1f1f1"
                        }

                    }

                }

            }

        });

}

/*********************************************************************
 *
 * Load Recent AI Generations
 *
 *********************************************************************/

async function loadRecentGenerations() {

    const tableBody =

        document.getElementById("recentGenerationBody");

    if (!tableBody)

        return;

    tableBody.innerHTML = `

<tr>

<td colspan="6" class="text-center">

<div class="spinner-border text-primary"></div>

</td>

</tr>

`;

    try {

        const response =

            await fetch("/api/dashboard/recent");

        if (!response.ok)

            throw new Error("Unable to load recent generations.");

        const rows =

            await response.json();

        if (rows.length === 0) {

            tableBody.innerHTML = `

<tr>

<td colspan="6"

class="text-center text-muted py-4">

No generation history found.

</td>

</tr>

`;

            return;

        }

        tableBody.innerHTML = "";

        rows.forEach(row => {

            tableBody.innerHTML += createRecentGenerationRow(row);

        });

    }

    catch (error) {

        console.error(error);

        tableBody.innerHTML = `

<tr>

<td colspan="6"

class="text-danger text-center py-4">

Unable to load recent generations.

</td>

</tr>

`;

    }

}


/*********************************************************************
 *
 * Create Recent Generation Row
 *
 *********************************************************************/

function createRecentGenerationRow(row) {

    const status =

        row.status === "SUCCESS"

            ?

            `<span class="badge bg-success">

                SUCCESS

            </span>`

            :

            `<span class="badge bg-danger">

                FAILED

            </span>`;

    return `

<tr>

<td>

<strong>

${row.storyKey}

</strong>

</td>

<td>

${row.generationType}

</td>

<td>

${status}

</td>

<td>

<span class="badge bg-primary">

${row.testCaseCount}

</span>

</td>

<td>

${row.aiModel}

</td>

<td>

${formatDate(row.createdAt)}

</td>

</tr>

`;

}


/*********************************************************************
 *
 * Format Local Date
 *
 *********************************************************************/

function formatDate(value) {

    if (!value)

        return "-";

    const date =

        new Date(value);

    return date.toLocaleString(

        "en-IN",

        {

            day: "2-digit",

            month: "short",

            year: "numeric",

            hour: "2-digit",

            minute: "2-digit"

        }

    );

}


/*********************************************************************
 *
 * Auto Refresh Dashboard
 *
 *********************************************************************/

function startAutoRefresh() {

    setInterval(() => {

        console.log("Refreshing dashboard...");

        loadRecentGenerations();

        loadChartData();

    }, 60000);

}

//---------------------------------------------------------
// Animated Counter
//---------------------------------------------------------

function animateCounter(id, target) {

    const element = document.getElementById(id);

    if (!element) return;

    let current = 0;

    const increment = Math.max(1, Math.ceil(target / 40));

    const timer = setInterval(() => {

        current += increment;

        if (current >= target) {

            current = target;

            clearInterval(timer);

        }

        element.textContent = current;

    }, 25);

}


function showToast(message, type) {

    document.getElementById("toastMessage").innerHTML = message;

    new bootstrap.Toast(

        document.getElementById("dashboardToast")

    ).show();

}

async function refreshDashboard() {

    await loadRecentGenerations();

    await loadChartData();

    updateLastRefresh();

    showToast(

        "Dashboard refreshed successfully",

        "success"

    );

}

function updateLastRefresh() {

    const now = new Date();

    document.getElementById("lastUpdated").innerHTML =

        now.toLocaleString();

}

function exportDashboard(type) {

    if (type === "excel") {

        showToast(

            "Excel export coming soon",

            "info"

        );

    }

    else {

        window.print();

    }

}