define(['jquery', 'bootstrap', 'backend', 'table', 'form'], function ($, undefined, Backend, Table, Form) {

    var Controller = {
        index: function () {
            // 初始化表格参数配置
            Table.api.init({
                extend: {
                    index_url: 'token_destroy/index' + location.search,
                    // add_url: 'token_destroy/add',
                    // edit_url: 'token_destroy/edit',
                    // del_url: 'token_destroy/del',
                    multi_url: 'token_destroy/multi',
                    import_url: 'token_destroy/import',
                    table: 'token_destroy',
                }
            });

            var table = $("#table");

            // 初始化表格
            table.bootstrapTable({
                url: $.fn.bootstrapTable.defaults.extend.index_url,
                pk: 'id',
                sortName: 'id',
                columns: [
                    [
                        // {checkbox: true},
                        {field: 'id', title: __('Id')},
                        {field: 'token', title: __('Token'), operate: false},
                        {field: 'day', title: __('Day'), operate:false, addclass:'datetimerange', autocomplete:false},
                        {field: 'last_amount', title: __('Last_amount'), operate:false},
                        {field: 'current_amount', title: __('Current_amount'), operate:false},
                        {field: 'create_time', title: __('Create_time'), operate:'RANGE', addclass:'datetimerange', autocomplete:false, formatter: Table.api.formatter.datetime},
                        // {field: 'operate', title: __('Operate'), table: table, events: Table.api.events.operate, formatter: Table.api.formatter.operate}
                    ]
                ]
            });

            // 为表格绑定事件
            Table.api.bindevent(table);
        },
        add: function () {
            Controller.api.bindevent();
        },
        edit: function () {
            Controller.api.bindevent();
        },
        api: {
            bindevent: function () {
                Form.api.bindevent($("form[role=form]"));
            }
        }
    };
    return Controller;
});
