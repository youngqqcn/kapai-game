define(['jquery', 'bootstrap', 'backend', 'table', 'form'], function ($, undefined, Backend, Table, Form) {

    var Controller = {
        index: function () {
            // 初始化表格参数配置
            Table.api.init({
                extend: {
                    index_url: 'new_wallet/index' + location.search,
                    // add_url: 'new_wallet/add',
                    // edit_url: 'new_wallet/edit',
                    // del_url: 'new_wallet/del',
                    multi_url: 'new_wallet/multi',
                    import_url: 'new_wallet/import',
                    table: 'wallet',
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
                        {field: 'wallet', title: __('Wallet'), operate: 'LIKE'},
                        // {field: 'ep', title: __('Ep'), operate:false},
                        {field: 'aTotalNumber', title: 'ART余额', operate:false},
                        {field: 'bTotalNumber', title: 'SOUL余额', operate:false},

                        {field: 'superior_id', title: '推荐人ID', operate:false},
                        {field: 'power', title: '静态算力', operate:false},
                        {field: 'dynamic_power', title: '动态算力', operate:false},
                        {field: 'b_power', title: 'SOUL静态算力', operate:false},
                        {field: 'dynamic_power', title: 'SOUL动态算力', operate:false},


                        {field: 'node_text', title: '个人节点身份', operate:false},
                        {field: 'lock_token_a', title: '锁仓ART数量', operate:false},
                        {field: 'node_power', title: '节点算力', operate:false},
                        // {field: 'child_node_level', title: '团队节点等级', operate:false},
                        // {field: 'child_buy_node_sum', title: '节点团队业绩（USDT）', operate:false},



                        {field: 'level', title: '团队等级', operate:false},
                        {field: 'zt_power', title: '直推动态算力', operate:false},
                        {field: 'small_power', title: '小区算力', operate:false},
                        {field: 'big_power', title: '大区算力', operate:false},
                        {field: 'team_dynamic_power', title: '团队动态算力', operate:false},


                        {field: 'status', title: __('Status'), searchList: {"0":__('Status 0'),"1":__('Status 1')}, formatter: Table.api.formatter.status},
                        // {field: 'update_time', title: __('Update_time'), operate:'RANGE', addclass:'datetimerange', autocomplete:false, formatter: Table.api.formatter.datetime},
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
