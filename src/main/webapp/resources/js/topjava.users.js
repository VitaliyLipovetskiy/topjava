const userAjaxUrl = "admin/users/";

// https://stackoverflow.com/a/5064235/548473
const ctx = {
    ajaxUrl: userAjaxUrl
};

// $(document).ready(function () {
$(function () {
    makeEditable(
        $("#datatable").DataTable({
            "paging": false,
            "info": true,
            "columns": [
                {
                    "data": "name"
                },
                {
                    "data": "email"
                },
                {
                    "data": "roles"
                },
                {
                    "data": "enabled"
                },
                {
                    "data": "registered"
                },
                {
                    "defaultContent": "Edit",
                    "orderable": false
                },
                {
                    "defaultContent": "Delete",
                    "orderable": false
                }
            ],
            "order": [
                [
                    0,
                    "asc"
                ]
            ]
        })
    );

    $(".user-enabled").click(function () {
        enableRow($(this).closest('tr').attr("id"));
    })

});

function enableRow(id) {
    $.post({
        url: ctx.ajaxUrl + "enabled",
        data: {"id": id}
    }).done(function () {
        $.get(ctx.ajaxUrl, function (data) {
            console.log(data);
            ctx.datatableApi.clear().rows.add(data).draw();
        });
        successNoty("Updated");
    });
}