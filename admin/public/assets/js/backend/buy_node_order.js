define(['jquery', 'bootstrap', 'backend', 'table', 'form'], function ($, undefined, Backend, Table, Form) {

    var Controller = {
        index: function () {
            // 初始化表格参数配置
            Table.api.init({
                extend: {
                    index_url: 'buy_node_order/index' + location.search,
                    // add_url: 'buy_node_order/add',
                    // edit_url: 'buy_node_order/edit',
                    // del_url: 'buy_node_order/del',
                    // multi_url: 'buy_node_order/multi',
                    import_url: 'buy_node_order/import',
                    table: 'buy_node_order',
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
                        {field: 'id', title: __('Id'),operate:false},
                        {field: 'wallet_id', title: '用户ID'},
                        {field: 'node_text', title: __('Node')},
                        {field: 'amount', title: '订单金额'},
                        {field: 'tx_hash', title: __('Tx_hash'), operate: 'LIKE'},
                        {field: 'status', title: __('Status'), searchList: {"-1":__('Status  -1'),"0":__('Status 0'),"1":__('Status 1'),"2":'订单创建'}, formatter: Table.api.formatter.status,operate:false},
                        {field: 'update_time', title: __('Update_time'), operate:false, addclass:'datetimerange', autocomplete:false, formatter: Table.api.formatter.datetime},
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
