// ===============================
// Dashboard Statistics
// ===============================

const stats = document.getElementById("dashboardStats");

const aiGenerations = Number(stats.dataset.ai || 0);
const uploads = Number(stats.dataset.uploads || 0);
const users = Number(stats.dataset.users || 0);
const audits = Number(stats.dataset.audits || 0);

// ===============================
// AI Usage Chart
// ===============================

new Chart(document.getElementById("usageChart"), {

    type: "bar",

    data: {

        labels: [
            "AI",
            "Uploads",
            "Users",
            "Audits"
        ],

        datasets: [{

            label: "Enterprise Statistics",

            data: [

                aiGenerations,
                uploads,
                users,
                audits

            ],

            borderWidth: 1

        }]

    },

    options: {

        responsive: true,

        maintainAspectRatio: false,

        plugins: {

            legend: {

                display: false

            }

        },

        scales: {

            y: {

                beginAtZero: true

            }

        }

    }

});

// ===============================
// Upload Statistics
// ===============================

new Chart(document.getElementById("uploadChart"), {

    type: "doughnut",

    data: {

        labels: [

            "Uploaded",
            "AI Generated"

        ],

        datasets: [{

            data: [

                uploads,
                aiGenerations

            ]

        }]

    },

    options: {

        responsive: true,

        maintainAspectRatio: false

    }

});
