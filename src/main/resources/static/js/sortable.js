function initializeSortable() {

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

}