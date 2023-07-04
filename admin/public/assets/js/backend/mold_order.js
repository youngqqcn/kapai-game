define(['jquery', 'bootstrap', 'backend', 'table', 'form'], function ($, undefined, Backend, Table, Form) {

    var Controller = {
        index: function () {
            // 初始化表格参数配置
            Table.api.init({
                extend: {
                    index_url: 'mold_order/index' + location.search,
                    // add_url: 'mold_order/add',
                    // edit_url: 'mold_order/edit',
                    // del_url: 'mold_order/del',
                    multi_url: 'mold_order/multi',
                    import_url: 'mold_order/import',
                    table: 'mold_order',
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
                        {field: 'id', title: __('Id'),operate: false},
                        {field: 'wallet_id', title: '会员ID'},
                        {field: 'card_model_id', title: __('Card_model_id')},
                        {field: 'quantity', title: __('Quantity'),operate: false},
                        // {field: 'ep', title: 'ep',operate: false},
                        {field: 'price', title: '价格',operate: false},
                        {field: 'type', title: '类型', searchList: {"0":'不使用IOT',"1":'使用IOT'}, formatter: Table.api.formatter.status},
                        {field: 'token_a', title: '消耗ART数量'},
                        {field: 'tx_hash', title: __('Tx_hash'), operate: 'LIKE'},
                        {field: 'status', title: __('Status'), searchList: {"-1":__('Status -1'),"0":__('Status 0'),"1":__('Status 1'),"2":"创建订单"}, formatter: Table.api.formatter.status},
                        {field: 'update_time', title: __('Update_time'), operate:'RANGE', addclass:'datetimerange', autocomplete:false, formatter: Table.api.formatter.datetime,operate: false},
                        {field: 'create_time', title: __('Create_time'), operate:'RANGE', addclass:'datetimerange', autocomplete:false, formatter: Table.api.formatter.datetime,operate: false},
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
