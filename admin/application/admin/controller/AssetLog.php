<?php

namespace app\admin\controller;

use app\common\controller\Backend;
use think\exception\DbException;
use think\response\Json;

/**
 * 资产变更记录
 *
 * @icon fa fa-circle-o
 */
class AssetLog extends Backend
{

    /**
     * AssetLog模型对象
     * @var \app\admin\model\AssetLog
     */
    protected $model = null;

    public function _initialize()
    {
        parent::_initialize();
        $this->model = new \app\admin\model\AssetLog;
        $this->view->assign("typeList", $this->model->getTypeList());
    }



    /**
     * 默认生成的控制器所继承的父类中有index/add/edit/del/multi五个基础方法、destroy/restore/recyclebin三个回收站方法
     * 因此在当前控制器中可不用编写增删改查的代码,除非需要自己控制这部分逻辑
     * 需要将application/admin/library/traits/Backend.php中对应的方法复制到当前控制器,然后进行修改
     */

    /**
     * 查看
     *
     * @return string|Json
     * @throws \think\Exception
     * @throws DbException
     */
    public function index()
    {
        //设置过滤方法
        $this->request->filter(['strip_tags', 'trim']);
        if (false === $this->request->isAjax()) {
            return $this->view->fetch();
        }
        //如果发送的来源是 Selectpage，则转发到 Selectpage
        if ($this->request->request('keyField')) {
            return $this->selectpage();
        }
        [$where, $sort, $order, $offset, $limit] = $this->buildparams();
        $identifying = !empty($_GET['identifying']) ? $_GET['identifying'] :0;
        if($identifying == 1){
            //ART释放记录
            $list = $this->model
                ->where("type = 2 and (source = 6 or source = 7)")
                ->where($where)
                ->order($sort, $order)
                ->paginate($limit);
        }else if($identifying == 2){
            //USDT团队分红记录
            $list = $this->model
                ->where("type = 6 and source = 8")
                ->where($where)
                ->order($sort, $order)
                ->paginate($limit);
        }else if($identifying == 3){
            //令牌产出记录
            $list = $this->model
                ->where("(type = 2 or type = 3) and (source = 3 or source = 4)")
                ->where($where)
                ->order($sort, $order)
                ->paginate($limit);
        }else{
            $list = $this->model
                ->where("type != 2 and type != 3")
                ->where($where)
                ->order($sort, $order)
                ->paginate($limit);
        }



        // 只需这样子就可以获取到可以循环的数组了
        $data = $list->all();


        if(!empty($data)){
            foreach ($data as $key => $value){
                $data[$key]['sourceText'] = self::getSourceText($value['type'],$value['source']);
            }
        }

        $result = ['total' => $list->total(), 'rows' => $data];
        return json($result);
    }


    public function usdt_log_list()
    {
        //设置过滤方法
        $this->request->filter(['strip_tags', 'trim']);
        if (false === $this->request->isAjax()) {
            return $this->view->fetch();
        }
        //如果发送的来源是 Selectpage，则转发到 Selectpage
        if ($this->request->request('keyField')) {
            return $this->selectpage();
        }
        [$where, $sort, $order, $offset, $limit] = $this->buildparams();
        $list = $this->model
            ->where("type = 6 and source = 8")
            ->where($where)
            ->order($sort, $order)
            ->paginate($limit);


        // 只需这样子就可以获取到可以循环的数组了
        $data = $list->all();


        if(!empty($data)){
            foreach ($data as $key => $value){
                $data[$key]['sourceText'] = self::getSourceText($value['type'],$value['source']);
            }
        }

        $result = ['total' => $list->total(), 'rows' => $data];
        return json($result);
    }

    public function art_log_list(){
        //设置过滤方法
        $this->request->filter(['strip_tags', 'trim']);
        if (false === $this->request->isAjax()) {
            return $this->view->fetch();
        }
        //如果发送的来源是 Selectpage，则转发到 Selectpage
        if ($this->request->request('keyField')) {
            return $this->selectpage();
        }
        [$where, $sort, $order, $offset, $limit] = $this->buildparams();
        $list = $this->model
            ->where("type = 2")
            ->where($where)
            ->order($sort, $order)
            ->paginate($limit);


        // 只需这样子就可以获取到可以循环的数组了
        $data = $list->all();


        if(!empty($data)){
            foreach ($data as $key => $value){
                $data[$key]['sourceText'] = self::getSourceText($value['type'],$value['source']);
            }
        }

        $result = ['total' => $list->total(), 'rows' => $data];
        return json($result);
    }

    public function soul_log_list(){
        //设置过滤方法
        $this->request->filter(['strip_tags', 'trim']);
        if (false === $this->request->isAjax()) {
            return $this->view->fetch();
        }
        //如果发送的来源是 Selectpage，则转发到 Selectpage
        if ($this->request->request('keyField')) {
            return $this->selectpage();
        }
        [$where, $sort, $order, $offset, $limit] = $this->buildparams();
        $list = $this->model
            ->where("type = 3")
            ->where($where)
            ->order($sort, $order)
            ->paginate($limit);


        // 只需这样子就可以获取到可以循环的数组了
        $data = $list->all();


        if(!empty($data)){
            foreach ($data as $key => $value){
                $data[$key]['sourceText'] = self::getSourceText($value['type'],$value['source']);
            }
        }

        $result = ['total' => $list->total(), 'rows' => $data];
        return json($result);
    }

    //卡牌产出记录
    public function ka_chan_log(){
        //设置过滤方法
        $this->request->filter(['strip_tags', 'trim']);
        if (false === $this->request->isAjax()) {
            return $this->view->fetch();
        }
        //如果发送的来源是 Selectpage，则转发到 Selectpage
        if ($this->request->request('keyField')) {
            return $this->selectpage();
        }
        [$where, $sort, $order, $offset, $limit] = $this->buildparams();
        //令牌产出记录
        $list = $this->model
            ->where("(type = 2 or type = 3) and (source = 3 or source = 4)")
            ->where($where)
            ->order($sort, $order)
            ->paginate($limit);


        // 只需这样子就可以获取到可以循环的数组了
        $data = $list->all();


        if(!empty($data)){
            foreach ($data as $key => $value){
                $data[$key]['sourceText'] = self::getSourceText($value['type'],$value['source']);
            }
        }

        $result = ['total' => $list->total(), 'rows' => $data];
        return json($result);
    }

    //获取来源描述
    public static function getSourceText($type,$source): string
    {
        $sourceText = '-';
        if($type == 1){
            if($source == 1){
                $sourceText = '铸造';
            }else if($source == 2){
                $sourceText = '直推奖励';
            }
        }else if($type == 2){
            if($source == 1){
                $sourceText = '提现';
            }else if($source == 3){
                $sourceText = '静态分红';
            }else if($source == 4){
                $sourceText = '动态分红';
            }else if($source == 5){
                $sourceText = '购买节点增加冻结数量';
            }else if($source == 6){
                $sourceText = '节点直推释放';
            }else if($source == 7){
                $sourceText = '节点动态释放';
            }else if($source == 8){
                $sourceText = 'USDT节点动态收益';
            }else if($source == 9){
                $sourceText = '节点分红';
            }
        }else if($type == 3){
            if($source == 1){
                $sourceText = '签到提现';
            }else if($source == 3){
                $sourceText = '静态分红';
            }else if($source == 4){
                $sourceText = '动态分红';
            }else if($source == 9){
                $sourceText = '节点分红';
            }
        }else if($type == 4){

        }else if($type == 5){
            if($source == 1){
                $sourceText = '兑换记录';
            }else if($source == 2){
                $sourceText = '铸造卡牌消耗EP';
            }else if($source == 3){
                $sourceText = '铸造卡牌失败归还EP';
            }
        }else if($type == 6){
            if($source == 8){
                $sourceText = '节点动态收益+USDT';
            }
        }
        return $sourceText;
    }
}
