<?php

namespace app\admin\model;

use think\Model;


class BuyNodeOrder extends Model
{

    

    

    // 表名
    protected $table = 'buy_node_order';
    
    // 自动写入时间戳字段
    protected $autoWriteTimestamp = false;

    // 定义时间戳字段名
    protected $createTime = false;
    protected $updateTime = false;
    protected $deleteTime = false;

    // 追加属性
    protected $append = [
        'status_text'
    ];
    

    
    public function getStatusList()
    {
        return ['-1' => __('Status  -1'), '0' => __('Status 0'), '1' => __('Status 1')];
    }


    public function getStatusTextAttr($value, $data)
    {
        $value = $value ? $value : (isset($data['status']) ? $data['status'] : '');
        $list = $this->getStatusList();
        return isset($list[$value]) ? $list[$value] : '';
    }

    //获取节点名称
    public static function getNodeText($node): string
    {
        if($node == 1){
            return '小节点';
        }else if($node == 2){
            return '大节点';
        }else if($node == 3){
            return '超级节点';
        }else{
            return '-';
        }
    }

}
