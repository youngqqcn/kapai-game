<?php

namespace app\admin\controller;

use app\common\controller\Backend;
use think\Db;
use think\exception\DbException;
use think\response\Json;

/**
 * 钱包
 *
 * @icon fa fa-circle-o
 */
class NewWallet extends Backend
{

    /**
     * NewWallet模型对象
     * @var \app\admin\model\NewWallet
     */
    protected $model = null;

    public function _initialize()
    {
        parent::_initialize();
        $this->model = new \app\admin\model\NewWallet;
        $this->view->assign("statusList", $this->model->getStatusList());
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

        //初始化redis对象实例
        $redis = new \Redis();
        //连接redis服务器
        $redis_server = config('redis.sever') ;
        $redis_auth = config('redis.auth');
        $redis->connect($redis_server,config('redis.port'));
        $redis->auth($redis_auth);

//        $data = $redis->hget('user:1','power');




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
            ->where($where)
            ->order($sort, $order)
            ->paginate($limit);

        foreach ($list as $k => $v) {


            //A代币余额
            $v->aTotalNumber = Db::table('check_in')->where(['wallet_id'=>$v['id'],'type'=>1,'status'=>0])->sum('amount');
            //b代币余额
            $v->bTotalNumber = Db::table('check_in')->where(['wallet_id'=>$v['id'],'type'=>2,'status'=>0])->sum('amount');

            $v->child_power = self::checkRedisValue($redis->hget("user:{$v->id}",'child_power'));
            $v->level = self::checkRedisValue($redis->hget("user:{$v->id}",'level'));
            $v->dynamic_power = self::checkRedisValue($redis->hget("user:{$v->id}",'dynamic_power'));
            $v->child_buy_node_sum = self::checkRedisValue($redis->hget("user:{$v->id}",'child_buy_node_sum'));
            $v->small_power = self::checkRedisValue($redis->hget("user:{$v->id}",'small_power'));
            $v->node_power = self::checkRedisValue($redis->hget("user:{$v->id}",'node_power'));
            $v->big_power = self::checkRedisValue($redis->hget("user:{$v->id}",'big_power'));
            $v->node = self::checkRedisValue($redis->hget("user:{$v->id}",'node'));

            $relation = Db::table('relation')->where(['wallet_id'=>$v->id])->find();
            if(!empty($relation['superior_id'])){
                $v->superior_id = $relation['superior_id'];
            }else{
                $v->superior_id = '-';
            }
//            $v->superior_id = self::checkRedisValue($redis->hget("user:{$v->id}",'superior_id'));


            $v->b_power = self::checkRedisValue($redis->hget("user:{$v->id}",'b_power'));
            $v->child_node_level = self::checkRedisValue($redis->hget("user:{$v->id}",'child_node_level'));
            $v->power = self::checkRedisValue($redis->hget("user:{$v->id}",'power'));
            $v->lock_token_a = self::checkRedisValue($redis->hget("user:{$v->id}",'lock_token_a'));
            $v->team_dynamic_power = self::checkRedisValue($redis->hget("user:{$v->id}",'team_dynamic_power'));
            $v->zt_power = self::checkRedisValue($redis->hget("user:{$v->id}",'zt_power'));
            $v->buy_node_sum = self::checkRedisValue($redis->hget("user:{$v->id}",'buy_node_sum'));
            $v->team_node_level = self::checkRedisValue($redis->hget("user:{$v->id}",'team_node_level'));

            $v->node_text = \app\admin\model\BuyNodeOrder::getNodeText($v->node);

        }
        $result = ['total' => $list->total(), 'rows' => $list->items()];
        return json($result);
    }


    public static function checkRedisValue($str){
        $str = str_replace('"', '', $str);
        if($str == 'false'){
            $str = 0;
        }
        return $str;
    }
}
